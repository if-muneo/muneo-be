package ureca.muneobe.common.chat.config.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.ai.openai.first")
public class OpenAiFirstPrompt {
    private String model;
    private double temperature;
    private int maxTokens;
    private String prompt;
}
