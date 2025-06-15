package ureca.muneobe.common.chat.service.strategy.rdb.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Condition {
    private MplanCondition mplanCondition;
    private AddonCondition addonCondition;
    private CombinedCondition combinedCondition;
}
