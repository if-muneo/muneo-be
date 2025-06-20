package ureca.muneobe.common.chat.service.strategy;

import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;

public interface RoutingStrategy {
    Flux<String> process(FirstPromptResult firstPromptResult, String memberName);
}
