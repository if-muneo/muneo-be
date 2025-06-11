package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.slang.service.SlangFilterService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;
    private final SlangFilterService slangFilterService;

    /**
     * 채팅 응답 생성
     */
    public Mono<String> createChatResponse(String username, String userMessage) {

        // 0. 금칙어 필터링 (추후 리팩토링 - 서비스가 서비스 참조 구조)
        if (slangFilterService.isSafe(userMessage)) {
            return Mono.just("부적절한 입력입니다.");
        }
        // 1. 채팅 저장
        saveChatToRedis(username, userMessage);

        // 2. 최근 채팅 내역 불러오기
        List<String> chatLog = getChatForMultiTurn(username);

        // 3. GPT 1차 프롬프트 호출
        return openAiClient.callFirstPrompt(userMessage, chatLog)
                .flatMap(intentJson -> {

                    // (부적절한 질문)
                    if ("INAPPROPRIATE".equalsIgnoreCase(intentJson.getRouter())) {
                        return Mono.just("부적절한 단어가 감지되었습니다. 다시 질문해주세요.");
                    }

                    // 5-1. RDB
                    if ("RDB".equalsIgnoreCase(intentJson.getRouter())) {

                        // 데이터 조회

                        // 2차 프롬프트 요청
                        // return rdbQueryService.findMatchingPlan(intentJson)
                        //        .flatMap(plan -> openAiClient.callSecondPrompt(userMessage, plan, intentJson));
                    }

                    // 5-2. VECTOR
                    if ("VECTOR".equalsIgnoreCase(intentJson.getRouter())) {

                        // 데이터 조회

                        // 2차 프롬프트 요청
//                          return vectorSearchService.queryRelevantDocs(intentJson)
//                                 .flatMap(docs -> openAiClient.callSecondPrompt(userMessage, docs, intentJson));
                    }

                    // 예외 상황 (응답이 비어있거나 등등)
                    return Mono.just("죄송합니다. 다시 질문해주세요.");

                })
                .onErrorResume(e -> {
                    log.error("응답 생성 중 예외 발생", e);
                    return Mono.just("시스템 오류가 발생했어요. 다시 시도해주세요.");
                });
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
