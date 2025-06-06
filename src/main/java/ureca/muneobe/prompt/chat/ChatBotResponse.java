package ureca.muneobe.prompt.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ChatBotResponse implements Serializable{
    private String content;
}
