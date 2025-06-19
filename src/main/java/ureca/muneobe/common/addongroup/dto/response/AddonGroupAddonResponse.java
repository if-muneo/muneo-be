package ureca.muneobe.common.addongroup.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addon.entity.AddonType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupAddonResponse {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private AddonType addonType;

    public static AddonGroupAddonResponse from(Addon addon){
        return AddonGroupAddonResponse.builder()
                .id(addon.getId())
                .name(addon.getName())
                .description(addon.getDescription())
                .price(addon.getPrice())
                .addonType(addon.getAddonType())
                .build();
    }
}
