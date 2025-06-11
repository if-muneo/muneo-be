package ureca.muneobe.common.addongroup.dto;

import java.util.List;
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
public class AddonGroupAddonsResponse {
    private List<AddonGroupAddonResponse> addonGroupAddonsResponse;

    public static AddonGroupAddonsResponse from(List<Addon> addons){
        return AddonGroupAddonsResponse.builder()
                .addonGroupAddonsResponse(addons.stream().map((AddonGroupAddonResponse::from)).toList())
                .build();
    }
}
