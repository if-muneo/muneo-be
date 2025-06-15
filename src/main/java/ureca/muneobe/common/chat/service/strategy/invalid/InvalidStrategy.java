package ureca.muneobe.common.chat.service.strategy.invalid;

import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

public class InvalidStrategy implements RoutingStrategy {
    @Override
    public Mono<String> process(FirstPromptResponse firstPromptResponse, String userMessage) {
        return Mono.just("죄송합니다. 다시 질문해주세요.");
    }
}
