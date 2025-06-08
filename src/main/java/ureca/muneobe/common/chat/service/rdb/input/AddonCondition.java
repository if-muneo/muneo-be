package ureca.muneobe.common.chat.service.rdb.input;

import java.util.List;
import lombok.Getter;
import ureca.muneobe.common.chat.entity.AddonType;

@Getter
public class AddonCondition {
    private Number price;
    private List<String> names;
    private AddonType addonType;
}
