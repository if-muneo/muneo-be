package ureca.muneobe.common.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.chat.entity.Chat;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.repository.ChatRepository;
import ureca.muneobe.common.chat.dto.result.ChatResult;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategyFactory;
import ureca.muneobe.common.openai.OpenAiClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;
    private final RoutingStrategyFactory routingStrategyFactory;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatMessagePreProcessor chatMessagePreProcessor;

    /**
     * 채팅 응답 생성
     */
    public Flux<String> createChatResponse(ChatResult chatResult) {
        return Mono.fromCallable(()->chatMessagePreProcessor.preProcess(chatResult))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(preProcessResult -> openAiClient.callFirstPrompt(preProcessResult))
                .flatMapMany(firstPromptResult -> searchAndCallSecondPrompt(firstPromptResult))
                .onErrorResume(Mono::error);
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

    private Flux<String> searchAndCallSecondPrompt(FirstPromptResult firstPromptResult) {
        RoutingStrategy routingStrategy = routingStrategyFactory.select(firstPromptResult);
        return routingStrategy.process(firstPromptResult);
    }
}
