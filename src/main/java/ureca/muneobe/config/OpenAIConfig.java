package ureca.muneobe.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class OpenAIConfig {

    @Value("${openai.api.models.first_request.model}")
    private String firstModel;

    @Value("${openai.api.models.second_request.model}")
    private String secondModel;

    @Bean
    public OpenAiService openAiService(@Value("${openai.api.key}") String apiKey) {
        return new OpenAiService(apiKey, Duration.ofSeconds(300));
    }
}
