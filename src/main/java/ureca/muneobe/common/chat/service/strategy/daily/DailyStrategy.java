package ureca.muneobe.common.chat.service.strategy.daily;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.openai.dto.router.DailyResponse;

@Component("dailyStrategy")
public class DailyStrategy implements RoutingStrategy {
    @Override
    public Flux<String> process(FirstPromptResult firstPromptResult, String memberName) {
        DailyResponse dailyResponse = (DailyResponse) firstPromptResult.getFirstPromptResponse();
        return Flux.just(dailyResponse.getReformInput());
    }
}
