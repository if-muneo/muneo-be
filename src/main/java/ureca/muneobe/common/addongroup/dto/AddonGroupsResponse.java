package ureca.muneobe.common.addongroup.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.chat.entity.AddonGroup;

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
