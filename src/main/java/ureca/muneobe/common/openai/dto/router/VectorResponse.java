package ureca.muneobe.common.openai.dto.router;

import lombok.Getter;

@Getter
public class VectorResponse extends FirstPromptResponse {
    private String reformInput;
}
