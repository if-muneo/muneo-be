package ureca.muneobe.common.chat.service.strategy.rdb.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ureca.muneobe.common.addon.entity.AddonType;

@AllArgsConstructor
@Builder
@ToString
@Getter
public class FindingAddon {
    private String name;
    private String description;
    private Integer price;
    private AddonType addonType;
}
