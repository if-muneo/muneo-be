package ureca.muneobe.common.mplan.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.mplan.dto.request.MplanDetailCreateRequest;
import ureca.muneobe.common.mplan.dto.response.MplanDetailCreateResponse;
import ureca.muneobe.common.mplan.dto.response.MplanDetailsResponse;
import ureca.muneobe.common.mplan.service.MplanDetailService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanDetailController {
    private final MplanDetailService mplanDetailService;

    @GetMapping("/v1/mplan-detail")
    public ResponseEntity<ResponseBody<MplanDetailsResponse>> readMplanDetails(
            @RequestParam(defaultValue = "0")int page
    ){
        return ResponseEntity.ok().body(ResponseBody.success(mplanDetailService.findAll(PageRequest.of(page, 4))));
    }

    @PostMapping("/v1/mplan-detail")
    public ResponseEntity<ResponseBody<MplanDetailCreateResponse>> createMplanDetail(
            @RequestBody MplanDetailCreateRequest mplanDetailCreateRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(ResponseBody.success(
                mplanDetailService.save(mplanDetailCreateRequest, SessionUtil.getLoginMember(httpSession).getRole())));
    }
}
