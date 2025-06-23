package ureca.muneobe.common.chat.dto.result;

import lombok.*;
import ureca.muneobe.common.chat.entity.ChatType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResult {
    private String memberName;
    private String message;
    private ChatType chatType;

    public static ChatResult of(final String memberName, final String message, final ChatType chatType) {
        return ChatResult.builder()
                .memberName(memberName)
                .message(message)
                .chatType(chatType)
                .build();
    }
}
