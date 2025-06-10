package ureca.muneobe.common.addongroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.dto.AddonGroupCreateRequest;
import ureca.muneobe.common.addongroup.dto.AddonGroupCreateResponse;
import ureca.muneobe.common.addongroup.dto.AddonGroupResponse;
import ureca.muneobe.common.addongroup.dto.AddonGroupsResponse;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.chat.entity.AddonGroup;

@Service
@RequiredArgsConstructor
public class AddonGroupService {
    private final AddonGroupRepository addonGroupRepository;

    public AddonGroupsResponse findAll(PageRequest pageRequest) {
        return getAddonGroupsResponse(pageRequest);
    }

    @Transactional
    public AddonGroupCreateResponse save(AddonGroupCreateRequest addonGroupCreateRequest) {
        return ;
    }

    private AddonGroupsResponse getAddonGroupsResponse(PageRequest pageRequest) {
        Page<AddonGroup> addonGroups = addonGroupRepository.findAll(pageRequest);
        return AddonGroupsResponse.builder()
                .addonGroupsResponse(addonGroups.map(AddonGroupResponse::from))
                .build();
    }
}
