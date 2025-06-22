package ureca.muneobe.common.chat.service.strategy.inappropriate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.OpenAiClient;

@Builder
@AllArgsConstructor
public class InappropriateResult implements RoutingResult {
    private String message;

    @Override
    public boolean skipSecondPrompt() {
        return true;
    }

    @Override
    public Flux<String> callSecondPromptOrNot(OpenAiClient openAiClient, MetaData metaData) {
        return Flux.just("부적절한 메시지가 들어왔음.");
    }
}
