package ureca.muneobe.common.chat.service.strategy.rdb;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import reactor.core.publisher.Flux;
import ureca.muneobe.common.chat.service.MetaData;
import ureca.muneobe.common.chat.service.strategy.RoutingResult;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;
import ureca.muneobe.common.openai.OpenAiClient;

@Builder
@AllArgsConstructor
public class RdbResult implements RoutingResult {
    private List<FindingMplan> findingMplans;
    @Override
    public Flux<String> callSecondPromptOrNot(OpenAiClient openAiClient, MetaData metaData) {
        return openAiClient.callSecondPrompt(findingMplans, metaData);
    }

    public static RdbResult from(List<FindingMplan> findingMplans){
        return RdbResult.builder()
                .findingMplans(findingMplans)
                .build();
    }
}
