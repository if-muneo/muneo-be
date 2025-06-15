package ureca.muneobe.common.chat.service.strategy.inappropriate;

import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

public class InappropriateStrategy implements RoutingStrategy {
    @Override
    public Mono<String> process(FirstPromptResponse firstPromptResponse, String userMessage) {
        return Mono.just("부적절한 단어가 감지되었습니다. 다시 질문해주세요.");
    }
}
