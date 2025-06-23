package ureca.muneobe.common.chat.service.strategy;

import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.openai.OpenAiClient;

public interface RoutingResult {
    Flux<String> callSecondPromptOrNot(OpenAiClient openAiClient, MetaData metaData);
}
