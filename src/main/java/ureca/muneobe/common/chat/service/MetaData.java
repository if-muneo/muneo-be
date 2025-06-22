package ureca.muneobe.common.chat.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.dto.result.ChatResult;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private ChatResult chatResult;
    private MemberInfoMeta memberInfoMeta;
    private List<String> chatLog;
}
