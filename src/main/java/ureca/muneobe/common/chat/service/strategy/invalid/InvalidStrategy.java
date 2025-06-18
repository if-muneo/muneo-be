package ureca.muneobe.common.chat.service.strategy.invalid;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;

@Component("invalidStrategy")
public class InvalidStrategy implements RoutingStrategy {
    @Override
    public Flux<String> process(FirstPromptResult firstPromptResult) {
        return Mono.just("죄송합니다. 다시 질문해주세요.").flux();
    }
}
