package ureca.muneobe.common.chat.service.strategy.inappropriate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

@Component
public class InappropriateStrategy {
    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        return Mono.just(new InappropriateResult("히히"));
    }
}
