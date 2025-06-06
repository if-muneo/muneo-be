package ureca.muneobe.prompt.chat;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String uri = request.getURI().toString();

        // 요청 uri에서 사용자 식별자(username) 뽑기 -> security를 사용하지 않기 때문에 직접 해줘야 함
        String username = UriComponentsBuilder.fromUriString(uri)
                .build()
                .getQueryParams()
                .getFirst("username");

        if (username == null) {
            return false;
        }

        attributes.put("username", username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
