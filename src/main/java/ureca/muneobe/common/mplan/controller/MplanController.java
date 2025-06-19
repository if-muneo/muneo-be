package ureca.muneobe.common.mplan.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;
import ureca.muneobe.common.mplan.dto.response.MplanCreateResponse;
import ureca.muneobe.common.mplan.dto.response.MplansResponse;
import ureca.muneobe.common.mplan.dto.response.UnapplyMplanCreateResponse;
import ureca.muneobe.common.mplan.service.MplanService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanController {
    private final MplanService mplanService;

    @GetMapping("/v1/mplan")
    public ResponseEntity<ResponseBody<MplansResponse>> readMplans(
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.findAll(PageRequest.of(page, 4))));
    }

    @PostMapping("/v1/mplan")
    public ResponseEntity<ResponseBody<UnapplyMplanCreateResponse>> createMplan(
            @RequestBody MplanCreateRequest mplanCreateRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok(ResponseBody.success(
                mplanService.save(mplanCreateRequest, SessionUtil.getLoginMember(httpSession).getRole())));
    }
}
