package ureca.muneobe.common.chat.dto;

public record StreamChatResponse(
        String streamId,
        String content,
        boolean done
) {
}
