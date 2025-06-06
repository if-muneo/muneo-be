package ureca.muneobe.prompt.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ureca.muneobe.prompt.chat.service.ChatService;
import ureca.muneobe.prompt.chat.dto.ChatResponse;
import ureca.muneobe.prompt.chat.dto.ChatRequest;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트가 /chat/sendMessage로 메시지 보내
    public void sendMessage(ChatRequest message, Principal principal) {
        String username = principal.getName();
        String userMessage = message.getContent();
        // 금칙어 필터링
        // Redis에서 이전 채팅 내역 가져오기(없으면 빈 리스트)
        List<String> preChat = chatService.getChatForMultiTurn(username);
        // Redis에 채팅 내역 저장
        chatService.saveChatToRedis(username, userMessage);
        // 1차 프롬프트로 메시지 넘기고 응답(JSON) 받음
        // RDB or VectorDB에 요청 넘기고 결과 받음
        // 2차 프롬프트에 결과 넘겨서 챗봇 메시지 만들기
        String chatBotMessage = "안녕하세요!"; // 챗봇의 최종 메시지
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/public", new ChatResponse(chatBotMessage));
    }
}
