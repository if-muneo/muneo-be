package ureca.muneobe.common.chat.service.strategy.invalid;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

@Component
public class InvalidStrategy {
    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        return Mono.just(new InvalidResult("부적절한 메시지입니다!"));                                                      //부적절한 메시지 반환
    }
}
