package ureca.muneobe.common.chat.service.strategy.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.common.openai.dto.router.VectorResponse;
import ureca.muneobe.common.vector.dto.response.VectorSearchResponse;
import ureca.muneobe.common.vector.service.FatService;

@Component("vectorStrategy")
@RequiredArgsConstructor
public class VectorStrategy{
    private final FatService fatService;

    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        VectorResponse response = (VectorResponse) firstPromptResponse;

        return Mono.fromCallable(() -> fatService.search(response.getReformInput()))
                .subscribeOn(Schedulers.boundedElastic())    // JPA 블로킹 호출을 별도 스레드풀에서 수행
                .map(VectorResult::from);
    }
}
