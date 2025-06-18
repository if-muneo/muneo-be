package ureca.muneobe.common.chat.dto.result;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
