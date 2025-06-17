package ureca.muneobe.common.addongroup.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupAddonsResponse;
import ureca.muneobe.common.addongroup.dto.request.AddonGroupCreateRequest;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupCreateResponse;
import ureca.muneobe.common.addongroup.dto.response.AddonGroupsResponse;
import ureca.muneobe.common.addongroup.service.AddonGroupService;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class AddonGroupController {
    private final AddonGroupService addonGroupService;

    @GetMapping("/v1/addon-group")
    public ResponseEntity<ResponseBody<AddonGroupsResponse>> readAddons(
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonGroupService.findAll(PageRequest.of(page, 8))));
    }

    @PostMapping("/v1/addon-group")
    public ResponseEntity<ResponseBody<AddonGroupCreateResponse>> createAddonGroup(
            @RequestBody AddonGroupCreateRequest addonGroupCreateRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(ResponseBody.success(
                addonGroupService.save(addonGroupCreateRequest, SessionUtil.getLoginMember(httpSession).getRole())));
    }

    @GetMapping("/v1/addon-group/{addonGroupId}")
    public ResponseEntity<ResponseBody<AddonGroupAddonsResponse>> readAddonGroupAddons(
            @PathVariable(name = "addonGroupId") Long addonGroupId
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonGroupService.findAddons(addonGroupId)));
    }
}
