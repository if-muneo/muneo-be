package ureca.muneobe.common.addongroup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.addongroup.dto.AddonGroupAddonsResponse;
import ureca.muneobe.common.addongroup.dto.AddonGroupCreateRequest;
import ureca.muneobe.common.addongroup.dto.AddonGroupCreateResponse;
import ureca.muneobe.common.addongroup.dto.AddonGroupResponse;
import ureca.muneobe.common.addongroup.dto.AddonGroupsResponse;
import ureca.muneobe.common.addongroup.service.AddonGroupService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class AddonGroupController {
    private final AddonGroupService addonGroupService;

    @GetMapping("/addon-group")
    public ResponseEntity<ResponseBody<AddonGroupsResponse>> readAddons(
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonGroupService.findAll(PageRequest.of(page, 10))));
    }

    @PostMapping("/addon-group")
    public ResponseEntity<ResponseBody<AddonGroupCreateResponse>> createAddonGroup(
            @RequestBody AddonGroupCreateRequest addonGroupCreateRequest
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonGroupService.save(addonGroupCreateRequest)));
    }

    @GetMapping("/addon-group/{addonGroupId}")
    public ResponseEntity<ResponseBody<AddonGroupAddonsResponse>> readAddonGroupAddons(
            @PathVariable(name = "addonGroupId") Long addonGroupId
    ){
        return ResponseEntity.ok().body(ResponseBody.success(addonGroupService.findAddons(addonGroupId)));
    }
}
