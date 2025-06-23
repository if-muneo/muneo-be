package ureca.muneobe.common.chat.service.strategy.rdb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.chat.repository.search.CombinedSearchRepository;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Condition;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.common.openai.dto.router.RdbResponse;

@Component("rdbStrategy")
@RequiredArgsConstructor
public class RdbStrategy {
    private final CombinedSearchRepository combinedSearchRepository;

    public Mono<RoutingResult> process(FirstPromptResponse firstPromptResponse, MetaData metaData) {
        RdbResponse response = (RdbResponse) firstPromptResponse;
        Condition condition = getCondition(response);

        return Mono.fromCallable(() -> combinedSearchRepository.search(condition))                                      //RDB 검색
                .subscribeOn(Schedulers.boundedElastic())                                                               //Blocking I/O를 boundedElastic 쓰레드에서 수행
                .map(RdbResult::from);                                                                                  //RdbResult impl RoutingResult로 매핑
    }

    private Condition getCondition(RdbResponse response) {
        return Condition.builder()
                .mplanCondition(response.getMplanCondition())
                .addonCondition(response.getAddonCondition())
                .combinedCondition(response.getCombinedCondition())
                .build();
    }
}
