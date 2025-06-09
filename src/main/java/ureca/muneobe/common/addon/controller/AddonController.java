package ureca.muneobe.common.addon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.addon.service.AddonService;

@RestController
@RequiredArgsConstructor
public class AddonController {
    private final AddonService addonService;


}
