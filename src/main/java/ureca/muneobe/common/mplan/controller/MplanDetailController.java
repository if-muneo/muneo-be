package ureca.muneobe.common.mplan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.mplan.dto.MplanDetailCreateRequest;
import ureca.muneobe.common.mplan.dto.MplanDetailCreateResponse;
import ureca.muneobe.common.mplan.dto.MplanDetailsResponse;
import ureca.muneobe.common.mplan.service.MplanDetailService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanDetailController {
    private final MplanDetailService mplanDetailService;

    @GetMapping("/mplan-details")
    public ResponseEntity<ResponseBody<MplanDetailsResponse>> readMplanDetails(
            @RequestParam(defaultValue = "0")int page
    ){
        return ResponseEntity.ok().body(ResponseBody.success(mplanDetailService.findAll(PageRequest.of(page, 10))));
    }

    @PostMapping("/mplan-detail")
    public ResponseEntity<ResponseBody<MplanDetailCreateResponse>> createMplanDetail(
            @RequestBody MplanDetailCreateRequest mplanDetailCreateRequest
    ){
        return ResponseEntity.ok().body(ResponseBody.success(mplanDetailService.save(mplanDetailCreateRequest)));
    }
}
