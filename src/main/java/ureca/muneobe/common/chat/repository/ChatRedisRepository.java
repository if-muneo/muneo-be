package ureca.muneobe.common.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {

    private static final int MULTI_TURN_CHAT_COUNT = 5;

    private final StringRedisTemplate stringTemplate;

    public void saveChat(String username, String userMessage) {
        stringTemplate.opsForList().leftPush(username, userMessage);
    }

    public List<String> findChat(String username) {
        return stringTemplate.opsForList().range(username, -MULTI_TURN_CHAT_COUNT, -1);
    }
}
