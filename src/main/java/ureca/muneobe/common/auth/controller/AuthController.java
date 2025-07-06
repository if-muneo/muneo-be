package ureca.muneobe.common.auth.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.auth.dto.request.LoginRequest;
import ureca.muneobe.common.auth.dto.request.SignupRequest;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.service.MemberService;
import ureca.muneobe.common.auth.utils.SessionUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest,
                                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        log.info(session.getId());
        try {
            Member member = memberService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            if (member != null) {
                SessionUtil.setMemberSession(session, member);

                response.put("success", true);
                response.put("message", "로그인 성공");
                response.put("id", member.getId());
                response.put("role", member.getRole());
                response.put("name", member.getName());

                return ResponseEntity.ok(response);

            } else {
                response.put("success", false);
                response.put("message", "아이디 또는 비밀번호가 잘못되었습니다");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "로그인 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        SessionUtil.logout(session);

        Map<String, Object> response = new HashMap<>();
        response.put("successed", true);
        response.put("message", "로그아웃되었습니다");
        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody SignupRequest signupRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            Member member = memberService.signup(signupRequest);
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다");
            response.put("id", member.getId());
            response.put("role", member.getRole());
            response.put("name", member.getName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);

        }
    }

    // 현재 로그인 상태 확인
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLoginStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (SessionUtil.isLoggedIn(session)) {
            Member member = SessionUtil.getLoginMember(session);
            response.put("loggedIn", true);
            response.put("user", member.getId());
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}
