package ureca.muneobe.common.addongroup.dto.response;

import lombok.*;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupResponse {
    private Long id;
    private String addonGroupName;

    public static AddonGroupResponse from(final AddonGroup addonGroup) {
        return AddonGroupResponse.builder()
                .id(addonGroup.getId())
                .addonGroupName(addonGroup.getAddonGroupName())
                .build();
    }
}
