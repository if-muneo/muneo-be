package ureca.muneobe.common.chat.service.strategy.inappropriate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

@Component
public class InappropriateStrategy {
    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        return Mono.just(new InappropriateResult("부적절한 메세지입니다! 요금제에 관련된 질문을 해주세요~"));                    //부적절한 응답
    }
}
