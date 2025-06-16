package ureca.muneobe.common.addon.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.addon.dto.request.DefaultAddonCreateRequest;
import ureca.muneobe.common.addon.dto.request.DefaultAddonSearchRequest;
import ureca.muneobe.common.addon.dto.response.DefaultAddonCreateResponse;
import ureca.muneobe.common.addon.dto.response.DefaultAddonResponse;
import ureca.muneobe.common.addon.dto.response.DefaultAddonsResponse;
import ureca.muneobe.common.addon.service.DefaultAddonService;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class DefaultAddonController {
    private final DefaultAddonService addonService;

    @GetMapping("/v1/addon")
    public ResponseEntity<ResponseBody<DefaultAddonsResponse>> readAddons(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(ResponseBody.success(addonService.findAll(PageRequest.of(page, 8))));
    }

    @GetMapping("/v1/addon/{addonId}")
    public ResponseEntity<ResponseBody<DefaultAddonResponse>> readAddon(
            @PathVariable Long addonId
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonService.findById(addonId)));
    }

    @PostMapping("/v1/addon")
    public ResponseEntity<ResponseBody<DefaultAddonCreateResponse>> createAddon(
            @RequestBody DefaultAddonCreateRequest defaultAddonCreateRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(ResponseBody.success(
                addonService.save(defaultAddonCreateRequest, SessionUtil.getLoginMember(httpSession).getRole())));
    }
}
