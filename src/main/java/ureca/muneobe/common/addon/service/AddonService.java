package ureca.muneobe.common.addon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.muneobe.common.addon.repository.AddonRepository;

@Service
@RequiredArgsConstructor
public class AddonService {
    private final AddonRepository addonRepository;
}
