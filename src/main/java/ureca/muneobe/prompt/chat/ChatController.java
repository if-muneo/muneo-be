package ureca.muneobe.prompt.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/message") // 클라이언트가 /chat/sendMessage로 메시지 보내
    public void sendMessage(ChatRequest message, Principal principal) {
        String userMessage = message.getContent();
        System.out.println("userMessage = " + userMessage);
        String username = principal.getName();
        // 금칙어 필터링
        // Redis에 채팅 내역 저장
        // 1차 프롬프트로 메시지 넘기고 응답(JSON) 받음
        // RDB or VectorDB에 요청 넘기고 결과 받음
        // 2차 프롬프트에 결과 넘겨서 챗봇 메시지 만들기
        String chatBotMessage = "안녕하세요!"; // 챗봇의 최종 메시지
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/public", new ChatBotResponse(chatBotMessage));
    }
}
