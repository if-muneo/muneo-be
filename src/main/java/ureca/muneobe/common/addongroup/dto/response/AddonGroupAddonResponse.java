package ureca.muneobe.common.addongroup.dto.response;

import lombok.*;
import ureca.muneobe.common.addon.entity.Addon;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupAddonResponse {
    private Long id;
    private String name;

    public static AddonGroupAddonResponse from(Addon addon){
        return AddonGroupAddonResponse.builder()
                .id(addon.getId())
                .name(addon.getName())
                .build();
    }
}
