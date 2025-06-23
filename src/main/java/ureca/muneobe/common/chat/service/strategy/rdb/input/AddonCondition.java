package ureca.muneobe.common.chat.service.strategy.rdb.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.addon.entity.AddonType;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AddonCondition {
    private Range price;
    private List<String> names;
    private List<AddonType> addonTypes;
}
