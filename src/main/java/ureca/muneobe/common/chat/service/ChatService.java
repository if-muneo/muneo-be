package ureca.muneobe.common.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.chat.entity.Chat;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.repository.ChatRepository;
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
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    /**
     * 채팅 응답 생성
     */
    public Mono<String> createChatResponse(String username, String userMessage) {
        //금칙어 필터링 (일단 보류)
        saveChatToRedis(username, userMessage, ChatType.REQUEST);
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
    /**
     * 채팅 Redis저장
     */
    private void saveChatToRedis(String username, String userMessage, ChatType chatType) {
        chatRedisRepository.saveChat(username, userMessage, chatType);
    }

    /**
     * 최근 채팅 내역 불러오기 (멀티턴)
     */
    private List<String> getChatForMultiTurn(String username) {
        return chatRedisRepository.findChat(username);
    }


    /**
     * 채팅 DB 저장
     */
    @Transactional
    public void saveChatLogToDB(String username) {

        // 유저 정보 가져오기
        Member member = memberRepository.findByName(username).orElse(null);

        List<Chat> chatLogs = chatRedisRepository.extractChatLogsFromRedis(member);

        if (chatLogs.isEmpty()) return;

        chatRepository.saveAll(chatLogs);
        chatRedisRepository.clearChat(username);
    }
}
