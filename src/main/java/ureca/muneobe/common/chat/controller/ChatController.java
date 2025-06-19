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

    @MessageMapping("/chat/message")
    public void sendMessage(ChatRequest message, Principal principal){
        String memberName = principal.getName();
        String userMessage = message.getContent();
        String streamId = message.getStreamId();


        if (containsSlang(userMessage)) {
            log.info("금칙어 감지");
            simpMessagingTemplate.convertAndSendToUser(
                    memberName,
                    "/queue/public",
                    StreamChatResponse.builder()
                            .streamId(streamId)
                            .content("부적절한 요청입니다.")
                            .done(true)
                            .build()
            );
            return;
        }

        chatService.createChatResponse(ChatResult.of(memberName, userMessage, ChatType.REQUEST))
                .subscribe(chatBotMessage -> {
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            URL,
                            StreamChatResponse.builder()
                                    .streamId(streamId)
                                    .content(chatBotMessage)
                                    .done(false)
                                    .build()
                    );
                }, error -> {
                    log.error("챗봇 응답 생성 실패", error);
                    simpMessagingTemplate.convertAndSendToUser(
                            memberName,
                            URL,
                            StreamChatResponse.builder()
                                    .streamId(streamId)
                                    .content("챗봇 응답 중 문제가 발생했어요!")
                                    .done(true)
                                    .build()
                    );
                }, () -> {
                        simpMessagingTemplate.convertAndSendToUser(
                                memberName,
                                URL,
                                StreamChatResponse.builder()
                                        .streamId(streamId)
                                        .content("")
                                        .done(true)
                                        .build()
                        );
                    }

                );
    }

    private boolean containsSlang(String userMessage) {
        return slangFilterService.filteringSlang(userMessage);
    }
}
