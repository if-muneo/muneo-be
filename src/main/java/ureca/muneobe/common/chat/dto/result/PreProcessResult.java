package ureca.muneobe.common.chat.dto.result;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreProcessResult {
    private String message;
    private List<String> chatLog;

    public static PreProcessResult of(final String message, final List<String> chatLog) {
        return PreProcessResult.builder()
                .message(message)
                .chatLog(chatLog)
                .build();

    }
}
