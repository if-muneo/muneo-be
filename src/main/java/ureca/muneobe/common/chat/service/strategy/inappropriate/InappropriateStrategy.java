package ureca.muneobe.common.chat.service.strategy.inappropriate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;

@Component("inappropriateStrategy")
public class InappropriateStrategy implements RoutingStrategy {
    @Override
    public Flux<String> process(FirstPromptResult firstPromptResult, String memberName) {
        return Mono.just("부적절한 단어가 감지되었습니다. 다시 질문해주세요.").flux();
    }
}
