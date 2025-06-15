package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategyFactory;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;

import static ureca.muneobe.global.response.ErrorCode.CHAT_RESPONSE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;
    private final RoutingStrategyFactory routingStrategyFactory;

    /**
     * 채팅 응답 생성
     */
    public Mono<String> createChatResponse(String username, String userMessage) {
        //금칙어 필터링 (일단 보류)
        saveChatToRedis(username, userMessage);
        List<String> chatLog = getChatForMultiTurn(username);
        return Mono.just(userMessage)
                .flatMap(message -> processFirstPrompt(message, chatLog))
                .flatMap(firstPromptResponse -> processSearchAndSecondPrompt(firstPromptResponse, userMessage))
                .onErrorResume(e -> Mono.error(new GlobalException(CHAT_RESPONSE_ERROR)));
    }

    private Mono<FirstPromptResponse> processFirstPrompt(String message, List<String> chatLog) {
        return openAiClient.callFirstPrompt(message, chatLog);
    }

    private Mono<String> processSearchAndSecondPrompt(FirstPromptResponse firstPromptResponse, String userMessage) {
        RoutingStrategy routingStrategy = routingStrategyFactory.select(firstPromptResponse);
        return routingStrategy.process(firstPromptResponse, userMessage);
    }

    private void saveChatToRedis(String username, String userMessage) {
        chatRedisRepository.saveChat(username, userMessage);
    }

    private List<String> getChatForMultiTurn(String username) {
        return chatRedisRepository.findChat(username);
    }
}
