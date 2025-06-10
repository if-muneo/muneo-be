package ureca.muneobe.common.addon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.addon.dto.DefaultAddonCreateRequest;
import ureca.muneobe.common.addon.dto.DefaultAddonCreateResponse;
import ureca.muneobe.common.addon.dto.DefaultAddonsResponse;
import ureca.muneobe.common.addon.service.DefaultAddonService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class DefaultAddonController {
    private final DefaultAddonService addonService;

    @GetMapping("/addon")
    public ResponseEntity<ResponseBody<DefaultAddonsResponse>> readAddons(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(ResponseBody.success(addonService.findAll(PageRequest.of(page, 10))));
    }

    @PostMapping("/addon")
    public ResponseEntity<ResponseBody<DefaultAddonCreateResponse>> createAddon(
            @RequestBody DefaultAddonCreateRequest defaultAddonCreateRequest
            ){
        return ResponseEntity.ok().body(ResponseBody.success(addonService.save(defaultAddonCreateRequest)));
    }
}
