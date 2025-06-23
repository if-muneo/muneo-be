package ureca.muneobe.common.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.chat.entity.Chat;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.repository.ChatRepository;
import ureca.muneobe.common.chat.service.strategy.daily.DailyStrategy;
import ureca.muneobe.common.chat.service.strategy.inappropriate.InappropriateStrategy;
import ureca.muneobe.common.chat.service.strategy.invalid.InvalidStrategy;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.chat.service.strategy.rdb.RdbStrategy;
import ureca.muneobe.common.chat.service.strategy.vector.VectorStrategy;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final OpenAiClient openAiClient;
    private final ChatRedisRepository chatRedisRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final RdbStrategy rdbStrategy;
    private final VectorStrategy vectorStrategy;
    private final DailyStrategy dailyStrategy;
    private final InappropriateStrategy inappropriateStrategy;
    private final InvalidStrategy invalidStrategy;

    /**
     * 채팅 응답 생성
     */
    public Flux<String> createChatResponse(final MetaData metaData) {
        return Mono.defer(() -> openAiClient.callFirstPrompt(metaData))                                                 //첫 번째 프롬프트를 호출한다. 결과 return
                .flatMap(firstPromptResponse -> routeStrategy(firstPromptResponse, metaData))                           //각 router 경로별 전략을 호출해, 결과를 return
                .flatMapMany(routingResult -> callSecondPrompt(routingResult, metaData));                                //만약 RDB나, VectorDB 라면 두번째 AI를 호출한다. 둘다 아니면, 기본 메시지를 return한다.
    }

    /**
     * 첫 프롬프트에 담긴 router 변수로 라우팅
     * 각 process는 RoutingResult 객체를 반환
     *
     */
    private Mono<RoutingResult> routeStrategy(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        return switch (firstPromptResponse.getRouter().toUpperCase()) {
            case "VECTOR"           -> vectorStrategy.process(firstPromptResponse, metaData);
            case "RDB"              -> rdbStrategy.process(firstPromptResponse, metaData);
            case "DAILY"            -> dailyStrategy.process(firstPromptResponse, metaData);
            case "INAPPROPRIATE"    -> inappropriateStrategy.process(firstPromptResponse, metaData);
            default                 -> invalidStrategy.process(firstPromptResponse, metaData);
        };
    }

    /**
     * 멀티턴을 위한 응답 저장
     * 스트리밍 방식 -> 청크들을 하나로 모아 저장(비동기)
     * 해당 메서드 체인만, boundedElastic 쓰레드 처리
     */
    private Flux<String> callSecondPrompt(RoutingResult routingResult, MetaData metaData) {
        final StringBuilder buffer = new StringBuilder();
        return routingResult.callSecondPromptOrNot(openAiClient, metaData)
                .concatMap(line ->
                        Mono.fromSupplier(() -> {
                            buffer.append(line);
                            return line;
                        }
                        ))
                .doOnComplete(() -> {
                    final String memberName = metaData.getMemberInfoMeta().getMemberName();
                    final String text = buffer.toString();
                    Mono
                            .fromRunnable(() -> chatRedisRepository.saveChat(memberName, text, ChatType.RESPONSE))
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                });
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
