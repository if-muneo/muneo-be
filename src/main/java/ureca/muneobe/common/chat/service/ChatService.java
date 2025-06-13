package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.repository.search.CombinedSearchRepository;
import ureca.muneobe.common.chat.service.rdb.input.Condition;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.DailyResponse;
import ureca.muneobe.common.openai.dto.router.RdbResponse;
import ureca.muneobe.common.openai.dto.router.VectorResponse;
import ureca.muneobe.common.vector.service.FatService;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;

import static ureca.muneobe.global.response.ErrorCode.CHAT_RESPONSE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;

    private final CombinedSearchRepository combinedSearchRepository;
    private final FatService fatService;

    /**
     * 채팅 응답 생성
     */
    public Mono<String> createChatResponse(String username, String userMessage) {

        long startTime = System.currentTimeMillis(); // ⏱ 시작 시각 기록
        // 0. 금칙어 필터링 (일단 보류)

        // 1. 채팅 저장
        saveChatToRedis(username, userMessage);

        // 2. 최근 채팅 내역 불러오기
        List<String> chatLog = getChatForMultiTurn(username);

        // 3. GPT 1차 프롬프트 호출
        return openAiClient.callFirstPrompt(userMessage, chatLog)
                .flatMap(firstPromptResponse -> {

                    log.info("어디로 갈까요? {}", firstPromptResponse.getRouter());

                    // (부적절한 질문)
                    if ("DAILY".equalsIgnoreCase(firstPromptResponse.getRouter())) {
                        DailyResponse dailyResponse = (DailyResponse) firstPromptResponse;
                        return Mono.just(dailyResponse.getReformInput());
                    }

                    // (부적절한 질문)
                    if ("INAPPROPRIATE".equalsIgnoreCase(firstPromptResponse.getRouter())) {
                        return Mono.just("부적절한 단어가 감지되었습니다. 다시 질문해주세요.");
                    }

                    // 5-1. RDB
                    if ("RDB".equalsIgnoreCase(firstPromptResponse.getRouter())) {

                        // 데이터 조회
                        RdbResponse response = (RdbResponse) firstPromptResponse;
                        final Condition condition = Condition.builder()
                                .mplanCondition(response.getMplanCondition())
                                .addonCondition(response.getAddonCondition())
                                .combinedCondition(response.getCombinedCondition())
                                .build();

                        // 2차 프롬프트
                        return Mono.fromCallable(() -> combinedSearchRepository.search(condition))
                                .subscribeOn(Schedulers.boundedElastic())    // JPA 블로킹 호출을 별도 스레드풀에서 수행
                                .flatMap(plans -> {
                                    if (plans == null || plans.isEmpty()) {
                                        // 검색 결과가 없을 때의 처리: Mono.empty()로 두거나, 다른 기본 메시지 반환
                                        return Mono.just("조건에 맞는 요금제가 없습니다.");
                                    }

                                    return openAiClient.callSecondPrompt(userMessage, plans);
                                })
                                .onErrorResume(e -> {
                                    // 에러 처리
                                    return Mono.just("요금제 검색 또는 2차 프롬프트 호출 중 오류가 발생했습니다.");
                                });
                    }

                    // 5-2. VECTOR
                    if ("VECTOR".equalsIgnoreCase(firstPromptResponse.getRouter())) {
                        // 데이터 조회
                        VectorResponse response = (VectorResponse) firstPromptResponse;
                        return Mono.fromCallable(() -> fatService.search(response.getReformInput()))
                                .subscribeOn(Schedulers.boundedElastic())    // JPA 블로킹 호출을 별도 스레드풀에서 수행
                                .flatMap(plans -> {
                                    if (plans == null || plans.isEmpty()) {
                                        // 검색 결과가 없을 때의 처리: Mono.empty()로 두거나, 다른 기본 메시지 반환
                                        return Mono.just("조건에 맞는 요금제가 없습니다.");
                                    }

                                    return openAiClient.callSecondPrompt(userMessage, plans);
                                })
                                .onErrorResume(e -> {
                                    // 에러 처리
                                    return Mono.just("요금제 검색 또는 2차 프롬프트 호출 중 오류가 발생했습니다.");
                                });
                    }

                    // 예외 상황 (응답이 비어있거나 등등)
                    return Mono.just("죄송합니다. 다시 질문해주세요.");

                })
                .doOnSuccess(result -> {
                    long endTime = System.currentTimeMillis(); // ⏱ 종료 시각
                    log.info("🔁 Chat 응답 생성 소요 시간: {} ms", (endTime - startTime));
                })
                .onErrorResume(e -> Mono.error(new GlobalException(CHAT_RESPONSE_ERROR)));
    }


    /**
     * 채팅 저장
     */
    private void saveChatToRedis(String username, String userMessage) {
        chatRedisRepository.saveChat(username, userMessage);
    }

    /**
     * 최근 채팅 내역 불러오기 (멀티턴)
     */
    private List<String> getChatForMultiTurn(String username) {
        return chatRedisRepository.findChat(username);
    }
}
