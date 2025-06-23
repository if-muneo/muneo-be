package ureca.muneobe.common.chat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.chat.dto.chat.StreamChatResponse;

@Component
@RequiredArgsConstructor
public class WebSocketResponseSender {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final String URL = "/queue/public";

    /**
     * 스트림 응답 전송 (진행 중)
     */
    public void sendStreamResponse(String memberName, String streamId, String content) {
        StreamChatResponse response = StreamChatResponse.builder()
                .streamId(streamId)
                .content(content)
                .done(false)
                .build();

        simpMessagingTemplate.convertAndSendToUser(memberName, URL, response);
    }

    /**
     * 에러 응답 전송 (완료)
     */
    public void sendErrorResponse(String memberName, String streamId, String errorMessage) {
        StreamChatResponse response = StreamChatResponse.builder()
                .streamId(streamId)
                .content(errorMessage)
                .done(true)
                .build();

        simpMessagingTemplate.convertAndSendToUser(memberName, URL, response);
    }

    /**
     * 완료 응답 전송
     */
    public void sendCompletionResponse(String memberName, String streamId) {
        StreamChatResponse response = StreamChatResponse.builder()
                .streamId(streamId)
                .content("")
                .done(true)
                .build();

        simpMessagingTemplate.convertAndSendToUser(memberName, URL, response);
    }

    /**
     * 커스텀 응답 전송
     */
    public void sendCustomResponse(String memberName, String streamId, String content, boolean isDone) {
        StreamChatResponse response = StreamChatResponse.builder()
                .streamId(streamId)
                .content(content)
                .done(isDone)
                .build();

        simpMessagingTemplate.convertAndSendToUser(memberName, URL, response);
    }
}