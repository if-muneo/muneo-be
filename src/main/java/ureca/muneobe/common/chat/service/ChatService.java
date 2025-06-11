package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

import java.util.List;

import static ureca.muneobe.global.response.ErrorCode.CHAT_RESPONSE_ERROR;
import static ureca.muneobe.global.response.ErrorCode.FIRST_PROMPT_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;


    /**
     * 채팅 응답 생성
     */
    public Mono<String> createChatResponse(String username, String userMessage) {

        // 0. 금칙어 필터링 (일단 보류)

        // 1. 채팅 저장
        saveChatToRedis(username, userMessage);

        // 2. 최근 채팅 내역 불러오기
        List<String> chatLog = getChatForMultiTurn(username);

        // 3. GPT 1차 프롬프트 호출
        return openAiClient.callFirstPrompt(userMessage, chatLog)
                .flatMap(firstPromptResponse -> {

                    // (부적절한 질문)
                    if ("INAPPROPRIATE".equalsIgnoreCase(firstPromptResponse.getRouter())) {
                        return Mono.just("부적절한 단어가 감지되었습니다. 다시 질문해주세요.");
                    }

                    // 5-1. RDB
                    if ("RDB".equalsIgnoreCase(firstPromptResponse.getRouter())) {

                        // 데이터 조회

                        // 2차 프롬프트 요청
                        // return rdbQueryService.findMatchingPlan(intentJson)
                        //        .flatMap(plan -> openAiClient.callSecondPrompt(userMessage, plan, intentJson));
                    }

                    // 5-2. VECTOR
                    if ("VECTOR".equalsIgnoreCase(firstPromptResponse.getRouter())) {

                        // 데이터 조회

                        // 2차 프롬프트 요청
                        //  return vectorSearchService.queryRelevantDocs(intentJson)
                        //         .flatMap(docs -> openAiClient.callSecondPrompt(userMessage, docs, intentJson));
                    }

                    // 예외 상황 (응답이 비어있거나 등등)
                    return Mono.just("죄송합니다. 다시 질문해주세요.");

                })
                .onErrorResume(e -> {
                    return Mono.error(new GlobalException(CHAT_RESPONSE_ERROR));
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
