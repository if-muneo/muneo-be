package ureca.muneobe.common.chat.service.strategy.vector;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.DailyResponse;
import ureca.muneobe.common.openai.dto.router.VectorResponse;
import ureca.muneobe.common.vector.service.FatService;

@Component("vectorStrategy")
@RequiredArgsConstructor
public class VectorStrategy implements RoutingStrategy {
    private final FatService fatService;
    private final OpenAiClient openAiClient;

    @Override
    public Flux<String> process(FirstPromptResult firstPromptResult, String memberName) {
        VectorResponse response = (VectorResponse) firstPromptResult.getFirstPromptResponse();
        String userMessage = firstPromptResult.getMessage();
        List<String> chatLog = firstPromptResult.getChatLog();

        return Mono.fromCallable(() -> fatService.search(response.getReformInput()))
                .subscribeOn(Schedulers.boundedElastic())    // JPA 블로킹 호출을 별도 스레드풀에서 수행
                .flatMapMany(plans -> {
                    if (plans == null || plans.getDescriptions() == null) return Mono.just("조건에 맞는 요금제가 없습니다.");
                    return openAiClient.callSecondPrompt(userMessage, plans.getDescriptions(), chatLog, memberName);
                })
                .onErrorResume(e -> Mono.just("요금제 검색 또는 2차 프롬프트 호출 중 오류가 발생했습니다."));
    }
}
