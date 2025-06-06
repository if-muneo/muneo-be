package ureca.muneobe.prompt.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ChatResponse implements Serializable{
    private String content;
}
