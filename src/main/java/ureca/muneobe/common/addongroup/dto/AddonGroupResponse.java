package ureca.muneobe.common.addongroup.dto;

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

    public static AddonGroupResponse from(final AddonGroup addonGroup) {
        return AddonGroupResponse.builder()
                .id(addonGroup.getId())
                .addonGroupName(addonGroup.getAddonGroupName())
                .build();
    }
}
