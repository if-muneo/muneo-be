package ureca.muneobe.common.mplan.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupAddonsResponse;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupResponse {
    private Long id;
    private String addonGroupName;
    private AddonGroupAddonsResponse addonGroupAddonsResponse;

    public static AddonGroupResponse from(final AddonGroup addonGroup) {
        return AddonGroupResponse.builder()
                .id(addonGroup.getId())
                .addonGroupName(addonGroup.getAddonGroupName())
                .addonGroupAddonsResponse(AddonGroupAddonsResponse.from(addonGroup.getAddons()))
                .build();
    }
}
