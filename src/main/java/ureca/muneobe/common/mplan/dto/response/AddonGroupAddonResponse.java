package ureca.muneobe.common.mplan.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.entity.Addon;
import ureca.muneobe.common.chat.entity.AddonType;

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

    public static AddonGroupAddonResponse from(final Addon addon){
        return AddonGroupAddonResponse.builder()
                .id(addon.getId())
                .name(addon.getName())
                .description(addon.getDescription())
                .price(addon.getPrice())
                .addonType(addon.getAddonType())
                .build();
    }
}
