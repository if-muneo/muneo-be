package ureca.muneobe.common.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ureca.muneobe.common.chat.dto.chat.ChatRequest;
import ureca.muneobe.common.chat.dto.chat.ChatResponse;
import ureca.muneobe.common.chat.dto.chat.StreamChatResponse;
import ureca.muneobe.common.chat.dto.result.ChatResult;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.chat.service.ChatService;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final String URL = "/queue/public";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatRequest message, Principal principal){
        String memberName = principal.getName();
        String userMessage = message.getContent();
        String streamId = message.getStreamId();

        chatService.createChatResponse(ChatResult.of(memberName, userMessage, ChatType.REQUEST))
                .subscribe(chatBotMessage -> {
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            URL,
                            new StreamChatResponse(streamId, chatBotMessage, false)
                    );
                }, error -> {
                    log.error("챗봇 응답 생성 실패", error);
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            URL,
                            new StreamChatResponse(streamId, "챗봇 응답 중 문제가 발생했어요!", true)
                    );
                }, () -> {
                        simpMessagingTemplate.convertAndSendToUser(
                                memberName,
                                URL,
                                new StreamChatResponse(streamId, "", true)
                        );
                    }

                );
    }
}
