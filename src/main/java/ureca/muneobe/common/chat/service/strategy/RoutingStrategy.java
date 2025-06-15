package ureca.muneobe.common.chat.service.strategy;

import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;

public interface RoutingStrategy {
    Mono<String> process(FirstPromptResult firstPromptResult);
}
