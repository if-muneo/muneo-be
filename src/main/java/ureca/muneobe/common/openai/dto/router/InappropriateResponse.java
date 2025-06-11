package ureca.muneobe.common.openai.dto.router;

import lombok.Getter;

@Getter
public class InappropriateResponse extends FirstPromptResponse {
    private String message;
}
