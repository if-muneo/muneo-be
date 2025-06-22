package ureca.muneobe.common.chat.service.strategy.daily;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.dto.router.DailyResponse;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

@Component
public class DailyStrategy {
    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData){
        DailyResponse dailyResponse = (DailyResponse) firstPromptResponse;
        return Mono.just(new DailyResult(dailyResponse));
    }
}
