package ureca.muneobe.common.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ureca.muneobe.common.chat.dto.chat.ChatRequest;
import ureca.muneobe.common.chat.dto.chat.StreamChatResponse;
import ureca.muneobe.common.chat.dto.result.ChatResult;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.chat.service.ChatService;
import ureca.muneobe.common.chat.service.MetaDataFactory;
import ureca.muneobe.common.slang.service.SlangFilterService;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final String URL = "/queue/public";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final SlangFilterService slangFilterService;
    private final MetaDataFactory metaDataFactory;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatRequest message, Principal principal){
        String memberName = principal.getName();
        String userMessage = message.getContent();
        String streamId = message.getStreamId();

        //금칙어 처리
        if (containsSlang(userMessage)) {
            sendMessage(memberName, streamId, "부적절한 메시지 입니다.", true);
            return;
        }


        chatService.createChatResponse(metaDataFactory.generate())
                .subscribe(
                        chatBotMessage -> sendMessage(memberName, streamId, chatBotMessage, false),
                        error -> sendMessage(memberName, streamId, "챗봇 응답 중 문제가 발생했어요.", true),
                        () -> sendMessage(memberName, streamId, "", true)
                );
    }

    private void sendMessage(String memberName, String streamId, String content, boolean isDone){
        simpMessagingTemplate.convertAndSendToUser(
                memberName,
                URL,
                StreamChatResponse.builder()
                        .streamId(streamId)
                        .content(content)
                        .done(isDone)
                        .build()
        );
    }

    private boolean containsSlang(String userMessage) {
        return slangFilterService.filteringSlang(userMessage);
    }
}
