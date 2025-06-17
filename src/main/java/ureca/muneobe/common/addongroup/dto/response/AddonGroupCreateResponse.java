package ureca.muneobe.common.addongroup.dto.response;

import lombok.*;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupCreateResponse {
    private Long id;

    public static AddonGroupCreateResponse from(AddonGroup addonGroup){
        return AddonGroupCreateResponse.builder()
                .id(addonGroup.getId())
                .build();
    }
}
