package ureca.muneobe.common.addongroup.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupsResponse {
    private Page<AddonGroupResponse> addonGroupsResponse;

    public static AddonGroupsResponse from(Page<AddonGroup> addonGroups){
        return AddonGroupsResponse.builder()
                .addonGroupsResponse(addonGroups.map(AddonGroupResponse::from))
                .build();
    }
}
