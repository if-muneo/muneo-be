package ureca.muneobe.common.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true) // 응답의 필요 없는 필드는 무시
public class OpenAiResponse {

    private List<Choice> choices;

    // 응답의 가장 첫번째 choice의 message.content
    public String getIntentJson() {
        if (choices != null && !choices.isEmpty()) {
            log.info("응답= {}", choices.get(0).getMessage().getContent().replaceAll("```","").replace("json",""));
            return choices.get(0).getMessage().getContent().replaceAll("```","").replace("json","");
        }
        return "{\"router\": \"NONE\"}";
    }

    // Getter & Setter
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    // 내부 클래스 정의
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
