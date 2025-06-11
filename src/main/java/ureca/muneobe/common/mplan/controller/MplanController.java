package ureca.muneobe.common.mplan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.mplan.dto.*;
import ureca.muneobe.common.mplan.service.MplanService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanController {
    private final MplanService mplanService;
    @PostMapping("/mplan")
    public ResponseEntity<ResponseBody<MplanCreateResponse>> createMplan(
            @RequestBody MplanCreateRequest mplanCreateRequest
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.save(mplanCreateRequest)));
    }
}
