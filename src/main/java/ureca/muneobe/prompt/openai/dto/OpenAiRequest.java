package ureca.muneobe.prompt.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    @JsonProperty("max_tokens")
    private int maxTokens;
}
