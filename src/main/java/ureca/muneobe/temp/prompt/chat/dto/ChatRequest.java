package ureca.muneobe.temp.prompt.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequest {

    private String content;
    private Long userId;
    private String timestamp;
}
