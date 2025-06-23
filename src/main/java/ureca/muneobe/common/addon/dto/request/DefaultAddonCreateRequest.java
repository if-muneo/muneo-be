package ureca.muneobe.common.addon.dto.request;

import lombok.*;
import ureca.muneobe.common.addon.entity.AddonType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAddonCreateRequest {
    private String name;
    private String description;
    private Integer price;
    private AddonType addonType;
}
