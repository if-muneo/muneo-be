package ureca.muneobe.common.mplan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.mplan.dto.*;
import ureca.muneobe.common.mplan.service.MplanService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanController {
    private final MplanService mplanService;

    @GetMapping("/mplan")
    public ResponseEntity<ResponseBody<MplansResponse>> readMplans(
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.findAll(PageRequest.of(page, 10))));
    }

    @PostMapping("/mplan")
    public ResponseEntity<ResponseBody<MplanCreateResponse>> createMplan(
            @RequestBody MplanCreateRequest mplanCreateRequest
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.save(mplanCreateRequest)));
    }
}
