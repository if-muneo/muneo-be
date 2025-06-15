package ureca.muneobe.common.chat.service.strategy;

import reactor.core.publisher.Mono;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

public interface RoutingStrategy {
    Mono<String> process(FirstPromptResponse firstPromptResponse, String userMessage);
}
