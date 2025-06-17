package ureca.muneobe.common.chat.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StreamChatResponse {
    private String streamId;
    private String content;
    boolean done;
}
