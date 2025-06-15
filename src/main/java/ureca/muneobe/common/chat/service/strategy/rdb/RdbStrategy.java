package ureca.muneobe.common.chat.service.strategy.rdb;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ureca.muneobe.common.chat.repository.search.CombinedSearchRepository;
import ureca.muneobe.common.chat.service.strategy.RoutingStrategy;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Condition;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.common.openai.dto.router.RdbResponse;

@Component
@RequiredArgsConstructor
public class RdbStrategy implements RoutingStrategy {
    private final CombinedSearchRepository combinedSearchRepository;
    private final OpenAiClient openAiClient;

    @Override
    public Mono<String> process(FirstPromptResponse firstPromptResponse, String userMessage) {
        RdbResponse response = (RdbResponse) firstPromptResponse;
        Condition condition = getCondition(response);

        return Mono.fromCallable(() -> combinedSearchRepository.search(condition))
                .subscribeOn(Schedulers.boundedElastic())    // JPA 블로킹 호출을 별도 스레드풀에서 수행
                .flatMap(plans -> {
                    if (plans == null || plans.isEmpty()) {
                        // 검색 결과가 없을 때의 처리: Mono.empty()로 두거나, 다른 기본 메시지 반환
                        return Mono.just("조건에 맞는 요금제가 없습니다.");
                    }

                    return openAiClient.callSecondPrompt(userMessage, plans);
                })
                .onErrorResume(e -> {
                    // 에러 처리
                    return Mono.just("요금제 검색 또는 2차 프롬프트 호출 중 오류가 발생했습니다.");
                });
    }

    private Condition getCondition(RdbResponse response) {
        return Condition.builder()
                .mplanCondition(response.getMplanCondition())
                .addonCondition(response.getAddonCondition())
                .combinedCondition(response.getCombinedCondition())
                .build();
    }
}
