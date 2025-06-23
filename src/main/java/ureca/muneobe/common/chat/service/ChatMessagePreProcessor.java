package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.chat.dto.chat.ChatRequest;
import ureca.muneobe.common.chat.dto.chat.MemberInfoMeta;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.util.MemberInfoMetaBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessagePreProcessor {
    private final ChatRedisRepository chatRedisRepository;
    private final MemberInfoMetaBuilder memberInfoMetaBuilder;

    /**
     * 멤버 정보를 가져옴
     * 레디스에 저장
     * chatLog 저장
     * 이들은 메타 정보가 되어, 모든 reactive streams에 정보를 제공한다.
     */
    public MetaData preProcess(ChatRequest chatRequest, String memberName) {
        List<String> chatLog = getChatForMultiTurn(memberName);
        saveChatToRedis(memberName, chatRequest.getContent(), ChatType.REQUEST);
        MemberInfoMeta memberInfoMeta = memberInfoMetaBuilder.buildFromMemberName(memberName);
        return MetaData.of(chatRequest, memberInfoMeta, chatLog);
    }

    /**
     * 채팅 Redis저장
     */
    private void saveChatToRedis(String memberName, String message, ChatType chatType) {
        chatRedisRepository.saveChat(memberName, message, chatType);
    }

    /**
     * 최근 채팅 내역 불러오기 (멀티턴)
     */
    private List<String> getChatForMultiTurn(String memberName) {
        return chatRedisRepository.findChat(memberName);
    }
}
