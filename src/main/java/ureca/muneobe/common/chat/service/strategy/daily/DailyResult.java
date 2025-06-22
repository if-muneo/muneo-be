package ureca.muneobe.common.chat.service.strategy.daily;

import lombok.AllArgsConstructor;
import lombok.Builder;
import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.openai.dto.router.DailyResponse;

@Builder
@AllArgsConstructor
public class DailyResult implements RoutingResult {
    private DailyResponse dailyResponse;

    @Override
    public boolean skipSecondPrompt() {
        return false;
    }

    @Override
    public Flux<String> callSecondPromptOrNot(OpenAiClient openAiClient, MetaData metaData) {
        return Flux.just(dailyResponse.getReformInput());
    }
}
