package ureca.muneobe.prompt.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.muneobe.prompt.chat.repository.ChatRedisRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRedisRepository chatRedisRepository;

    public void saveChatToRedis(String username, String userMessage) {
        chatRedisRepository.saveChat(username, userMessage);
    }

    public List<String> getChatForMultiTurn(String username) {
        return chatRedisRepository.findChat(username);
    }
}
