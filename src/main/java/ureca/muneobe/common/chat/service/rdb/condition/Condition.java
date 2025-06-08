package ureca.muneobe.common.chat.service.rdb.condition;

import java.util.List;
import lombok.Getter;

@Getter
public class Condition {
    private MplanCondition mplanCondition;
    private List<AddonCondition> addonCondition;
    private CombinedCondition combinedCondition;
}
