package ureca.muneobe.common.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ureca.muneobe.common.chat.dto.ChatRequest;
import ureca.muneobe.common.chat.dto.StreamChatResponse;
import ureca.muneobe.common.chat.service.ChatService;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트가 /chat/sendMessage로 메시지 보내
    public void sendMessage(ChatRequest message, Principal principal){
        String memberName = principal.getName();
        String userMessage = message.getContent();
        String streamId = message.getStreamId();

        chatService.createChatResponse(memberName, userMessage)
                .subscribe(chatChunk -> {
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            "/queue/public",
                            new StreamChatResponse(streamId, chatChunk, false) // chunk 단위
                    );
                }, error -> {
                    log.error("챗봇 응답 생성 실패", error);
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            "/queue/public",
                            new StreamChatResponse(streamId, "챗봇 응답 중 문제가 발생했어요!", true)
                    );
                }, () -> {
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            "/queue/public",
                            new StreamChatResponse(streamId, "", true)
                    );
                });
    }
}
