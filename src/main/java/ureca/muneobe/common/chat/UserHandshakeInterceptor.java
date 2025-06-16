package ureca.muneobe.common.chat;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.utils.SessionUtil;

import java.util.Map;

public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String uri = request.getURI().toString();
//
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpSession session = servletRequest.getServletRequest().getSession(false);
//
//            // SessionUtil로 로그인 확인
//            if (SessionUtil.isLoggedIn(session)) {
//                Member member = SessionUtil.getLoginMember(session);
//
//                // 웹소켓 세션에 사용자 정보 저장
//                attributes.put("member", member);
//                attributes.put("memberName", member.getName());
//                attributes.put("memberId", member.getId());
//
//                return true; // 연결 허용
//            }
//        }
//
//        return false; // 로그인하지 않은 사용자는 연결 거부

        // 프론트 연동 전까지 테스트
        attributes.put("username", "박상윤");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
