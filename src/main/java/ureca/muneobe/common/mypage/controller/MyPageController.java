package ureca.muneobe.common.mypage.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.mypage.dto.response.MyPageResponse;
import ureca.muneobe.common.mypage.service.MyPageService;
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/v1/mypage")
    public ResponseEntity<ResponseBody<MyPageResponse>> readMyPage(
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(
                ResponseBody.success(myPageService.findMyInfo(SessionUtil.getLoginMember(httpSession))));
    }
}
