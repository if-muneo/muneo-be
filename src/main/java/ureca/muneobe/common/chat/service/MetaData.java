package ureca.muneobe.common.chat.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.dto.chat.ChatRequest;
import ureca.muneobe.common.chat.dto.chat.MemberInfoMeta;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private ChatRequest chatRequest;
    private MemberInfoMeta memberInfoMeta;
    private List<String> chatLog;

    public static MetaData of(ChatRequest chatRequest, MemberInfoMeta memberInfoMeta, List<String> chatLog){
        return MetaData.builder()
                .chatRequest(chatRequest)
                .memberInfoMeta(memberInfoMeta)
                .chatLog(chatLog)
                .build();
    }
}
