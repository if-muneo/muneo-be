package ureca.muneobe.common.openai.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;

    public static Message from(String role, String content){
        return Message.builder()
                .role(role)
                .content(content)
                .build();
    }
}
