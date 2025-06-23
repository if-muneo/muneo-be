package ureca.muneobe.common.chat.service.strategy.vector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.openai.OpenAiClient;
import ureca.muneobe.common.vector.dto.response.VectorSearchResponse;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorResult implements RoutingResult {
    private VectorSearchResponse vectorSearchResponse;
    @Override
    public Flux<String> callSecondPromptOrNot(OpenAiClient openAiClient, MetaData metaData) {
        return openAiClient.callSecondPrompt(vectorSearchResponse.getDescriptions(), metaData);
    }

    public static VectorResult from(VectorSearchResponse vectorSearchResponse) {
        return VectorResult.builder()
                .vectorSearchResponse(vectorSearchResponse)
                .build();
    }
}
