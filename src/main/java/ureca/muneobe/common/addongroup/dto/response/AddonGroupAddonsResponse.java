package ureca.muneobe.common.addongroup.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupAddonsResponse {
    private Long id;
    private String name;
    private List<AddonGroupAddonResponse> addonGroupAddonsResponse;

    public static AddonGroupAddonsResponse from(List<Addon> addons){
        return AddonGroupAddonsResponse.builder()
                .addonGroupAddonsResponse(addons.stream().map((AddonGroupAddonResponse::from)).toList())
                .build();
    }

    public static AddonGroupAddonsResponse of(AddonGroup addonGroup, List<Addon> addons) {
        return AddonGroupAddonsResponse.builder()
                .id(addonGroup.getId())
                .name(addonGroup.getAddonGroupName())
                .addonGroupAddonsResponse(addons.stream().map((AddonGroupAddonResponse::from)).toList())
                .build();
    }
}
