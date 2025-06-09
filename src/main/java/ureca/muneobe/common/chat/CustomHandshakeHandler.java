package ureca.muneobe.common.chat;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ureca.muneobe.common.auth.entity.Member;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        Member member = (Member) attributes.get("member");
        if (member != null) {
            return () -> member.getName(); // Principal.getName()에 사용됨
        }

        return null;
    }
}
