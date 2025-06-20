package ureca.muneobe.common.chat.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ureca.muneobe.common.chat.service.ChatService;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatService chatService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        log.info("Disconnected from websocket");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // 웹소켓 세션에 저장된 사용자 정보 가져오기
        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            chatService.saveChatLogToDB(username);
        }
    }
}
