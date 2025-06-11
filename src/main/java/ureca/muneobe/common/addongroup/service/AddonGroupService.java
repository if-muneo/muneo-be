package ureca.muneobe.common.addongroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.dto.*;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.chat.entity.Addon;
import ureca.muneobe.common.chat.entity.AddonGroup;
import ureca.muneobe.common.chat.repository.AddonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonGroupService {
    private final AddonGroupRepository addonGroupRepository;
    private final AddonRepository addonRepository;

    public AddonGroupsResponse findAll(PageRequest pageRequest) {
        return getAddonGroupsResponse(pageRequest);
    }

    @Transactional
    public AddonGroupCreateResponse save(AddonGroupCreateRequest addonGroupCreateRequest) {
        AddonGroup savedAddonGroup = addonGroupRepository.save(getAddonGroup(addonGroupCreateRequest));
        List<Addon> addons = getAddons(addonGroupCreateRequest, savedAddonGroup.getId());
        List<Addon> savedAddons = addonRepository.saveAll(addons);   //배치처리하기(JDBC로)
        return getAddonGroupCreateResponse(savedAddonGroup);
    }

    private AddonGroupsResponse getAddonGroupsResponse(PageRequest pageRequest) {
        Page<AddonGroup> addonGroups = addonGroupRepository.findAll(pageRequest);
        return AddonGroupsResponse.builder().addonGroupsResponse(addonGroups.map(AddonGroupResponse::from))
                .build();
    }

    private AddonGroup getAddonGroup(AddonGroupCreateRequest addonGroupCreateRequest) {
        return AddonGroup.from(addonGroupCreateRequest);
    }

    private List<Addon> getAddons(AddonGroupCreateRequest addonGroupCreateRequest, Long addonGroupId) {
        List<AddonCreateRequest> addonCreateRequests = addonGroupCreateRequest.getAddonsCreateRequest();
        return addonCreateRequests.stream().map(addonCreateRequest ->
            Addon.of(addonCreateRequest, addonGroupRepository.getReferenceById(addonGroupId))).toList();
    }

    private AddonGroupCreateResponse getAddonGroupCreateResponse(AddonGroup addonGroup) {
        return AddonGroupCreateResponse.from(addonGroup);
    }
}
