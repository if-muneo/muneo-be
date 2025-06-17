package ureca.muneobe.common.openai.dto.router;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ureca.muneobe.common.chat.service.strategy.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.CombinedCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.MplanCondition;

@Getter
@AllArgsConstructor
public class RdbResponse extends FirstPromptResponse {
    private String userInput;
    private MplanCondition mplanCondition;
    private AddonCondition addonCondition;
    private CombinedCondition combinedCondition;
}
