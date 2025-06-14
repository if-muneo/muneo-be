package ureca.muneobe.common.addongroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.dto.request.AddonCreateRequest;
import ureca.muneobe.common.addongroup.dto.request.AddonGroupCreateRequest;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupAddonsResponse;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupCreateResponse;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupsResponse;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.chat.entity.Addon;
import ureca.muneobe.common.chat.entity.AddonGroup;
import ureca.muneobe.common.chat.repository.AddonRepository;

import java.util.List;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class AddonGroupService {
    private final AddonGroupRepository addonGroupRepository;
    private final AddonRepository addonRepository;

    public AddonGroupsResponse findAll(PageRequest pageRequest) {
        return getAddonGroupsResponse(pageRequest);
    }

    public AddonGroupAddonsResponse findAddons(Long addonGroupId) {
        return getAddonGroupAddonsResponse(addonGroupId);
    }

    @Transactional
    public AddonGroupCreateResponse save(AddonGroupCreateRequest addonGroupCreateRequest, Role role) {
        if(role == Role.USER)throw new GlobalException(ErrorCode.FORBIDDEN);
        AddonGroup savedAddonGroup = addonGroupRepository.save(getAddonGroup(addonGroupCreateRequest));
        List<Addon> addons = getAddons(addonGroupCreateRequest, savedAddonGroup.getId());
        List<Addon> savedAddons = addonRepository.saveAll(addons);   //배치처리하기(JDBC로)
        return getAddonGroupCreateResponse(savedAddonGroup);
    }

    private AddonGroupsResponse getAddonGroupsResponse(PageRequest pageRequest) {
        Page<AddonGroup> addonGroups = addonGroupRepository.findAll(pageRequest);
        return AddonGroupsResponse.from(addonGroups);
    }

    private AddonGroupAddonsResponse getAddonGroupAddonsResponse(Long addonGroupId) {
        List<Addon> addons = addonRepository.findAddonsByAddonGroup(addonGroupRepository.getReferenceById(addonGroupId));
        return AddonGroupAddonsResponse.from(addons);
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
