package ureca.muneobe.common.addon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addon.dto.DefaultAddonCreateRequest;
import ureca.muneobe.common.addon.dto.DefaultAddonCreateResponse;
import ureca.muneobe.common.addon.dto.DefaultAddonResponse;
import ureca.muneobe.common.addon.dto.DefaultAddonsResponse;
import ureca.muneobe.common.addon.entity.DefaultAddon;
import ureca.muneobe.common.addon.repository.DefaultAddonRepository;

@Service
@RequiredArgsConstructor
public class DefaultAddonService {
    private final DefaultAddonRepository defaultAddonRepository;

    public DefaultAddonsResponse findAll(PageRequest pageRequest) {
        return getAddonsResponse(pageRequest);
    }

    @Transactional
    public DefaultAddonCreateResponse save(DefaultAddonCreateRequest defaultAddonCreateRequest) {
        DefaultAddon savedDefaultAddon = defaultAddonRepository.save(getDefaultAddon(defaultAddonCreateRequest));
        return getDefaultAddonCreateResponse(savedDefaultAddon.getId());
    }

    private DefaultAddonsResponse getAddonsResponse(PageRequest pageRequest) {
        Page<DefaultAddon> defaultAddons = defaultAddonRepository.findAll(pageRequest);
        return DefaultAddonsResponse.from(defaultAddons);
    }

    private DefaultAddon getDefaultAddon(DefaultAddonCreateRequest defaultAddonCreateRequest) {
        return DefaultAddon.from(defaultAddonCreateRequest);
    }

    private DefaultAddonCreateResponse getDefaultAddonCreateResponse(Long id){
        return DefaultAddonCreateResponse.from(id);
    }
}
