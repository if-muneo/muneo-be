package ureca.muneobe.common.addongroup.dto;

import lombok.*;
import ureca.muneobe.common.chat.entity.AddonGroup;

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
