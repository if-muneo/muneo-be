package ureca.muneobe.common.mplan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.mplan.dto.MplanAddRequest;
import ureca.muneobe.common.mplan.dto.MplanRequest;
import ureca.muneobe.common.mplan.dto.MplansRequest;
import ureca.muneobe.common.mplan.dto.SimpMplanResponse;
import ureca.muneobe.common.mplan.dto.SimpMplansResponse;
import ureca.muneobe.common.mplan.service.MplanService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MplanController {
    private final MplanService mplanService;

    @GetMapping("/mplan")   //10개씩 mplan 보여주기
    public ResponseEntity<ResponseBody<SimpMplansResponse>> getMplan(
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.findAll(PageRequest.of(page, 10))));
    }

    @GetMapping("/mplan/{mplanId}") //10개중 하나 클릭 시
    public ResponseEntity<ResponseBody<SimpMplanResponse>> getMplanById(
            @PathVariable Long mplanId
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.findById(mplanId)));
    }

    @PostMapping("/mplan")  //관리자는 mplan을 추가할 수 있다.
    public ResponseEntity<ResponseBody<Void>> createMplan(
            @RequestBody MplanAddRequest mplanAddRequest,
            @SessionAttribute(name = "role", required = true)Role role
            ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.saveMplan(mplanRequest, role)));
    }

    @PutMapping("/mplan")   //관리자는 mplan을 수정할 수 있다.
    public ResponseEntity<ResponseBody<Void>> updateMplan(
            @RequestBody MplanRequest mplanRequest,
            @SessionAttribute(name = "role", required = true)Role role
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.updateMplan(mplanRequest, role)));
    }

    @DeleteMapping("/mplan/{mplanId}")  //관리자는 mplan을 삭제할 수 있다.
    public ResponseEntity<ResponseBody<Void>> deleteMplan(
            @PathVariable Long mplanId,
            @SessionAttribute(name = "role", required = true)Role role
    ){
        return ResponseEntity.ok(ResponseBody.success(mplanService.deleteMplanById(mplanId, role)));
    }
}
