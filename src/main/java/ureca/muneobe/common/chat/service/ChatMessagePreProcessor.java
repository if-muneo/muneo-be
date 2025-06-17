package ureca.muneobe.common.chat.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.chat.dto.result.ChatResult;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.common.chat.repository.ChatRedisRepository;
import ureca.muneobe.common.chat.dto.result.PreProcessResult;

@Component
@RequiredArgsConstructor
public class ChatMessagePreProcessor {
    private final ChatRedisRepository chatRedisRepository;

    public PreProcessResult preProcess(ChatResult chatResult) {
        System.out.println(Thread.currentThread().getName());
        saveChatToRedis(chatResult.getMemberName(), chatResult.getMessage(), chatResult.getChatType());
        List<String> chatLog = getChatForMultiTurn(chatResult.getMemberName());
        return PreProcessResult.of(chatResult.getMessage(), chatLog);
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
