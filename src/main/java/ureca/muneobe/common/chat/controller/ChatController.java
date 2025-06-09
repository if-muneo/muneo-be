package ureca.muneobe.common.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ureca.muneobe.common.chat.dto.ChatRequest;
import ureca.muneobe.common.chat.dto.ChatResponse;
import ureca.muneobe.common.chat.service.ChatService;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트가 /chat/sendMessage로 메시지 보내
    public void sendMessage(ChatRequest message, Principal principal){
        String username = principal.getName();
        String userMessage = message.getContent();

        log.info("message : {}", message.getContent());

        chatService.createChatResponse(username, userMessage)
                .subscribe(chatBotMessage -> {
                    // Mono 완료 후 사용자에게 WebSocket 응답 전송
                    simpMessagingTemplate.convertAndSendToUser(
                            username,
                            "/queue/public",
                            new ChatResponse(chatBotMessage)
                    );
                }, error -> {
                    log.error("챗봇 응답 생성 실패", error);
                    simpMessagingTemplate.convertAndSendToUser(
                            username,
                            "/queue/public",
                            new ChatResponse("챗봇 응답 중 문제가 발생했어요!")
                    );
                });
    }
}
