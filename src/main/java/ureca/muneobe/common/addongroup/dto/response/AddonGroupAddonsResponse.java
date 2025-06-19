package ureca.muneobe.common.addongroup.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addon.entity.Addon;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupAddonsResponse {
    private List<AddonGroupAddonResponse> addonGroupAddonsResponse;

    public static AddonGroupAddonsResponse from(List<Addon> addons){
        return AddonGroupAddonsResponse.builder()
                .addonGroupAddonsResponse(addons.stream().map((AddonGroupAddonResponse::from)).toList())
                .build();
    }
}
