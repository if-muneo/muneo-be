package ureca.muneobe.common.mplan.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupResponse {
    private Long id;
    private String addonGroupName;
    private List<AddonGroupAddonResponse> addonGroupAddonsResponse;

    public static AddonGroupResponse from(final AddonGroup addonGroup) {
        return AddonGroupResponse.builder()
                .id(addonGroup.getId())
                .addonGroupName(addonGroup.getAddonGroupName())
                .addonGroupAddonsResponse(addonGroup.getAddons())
                .build();
    }
}
