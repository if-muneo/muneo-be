package ureca.muneobe.common.chat.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirstPromptResult {
    private FirstPromptResponse firstPromptResponse;
    private List<String> chatLog;
    private String message;

    public static FirstPromptResult of(final FirstPromptResponse firstPromptResponse, String message, List<String> chatLog) {
        return FirstPromptResult.builder()
                .firstPromptResponse(firstPromptResponse)
                .message(message)
                .build();
    }
}
