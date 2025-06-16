package ureca.muneobe.common.chat.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;
import java.util.Map;

import static ureca.muneobe.global.response.ErrorCode.REDIS_CHAT_STORE_ERROR;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {

    private static final int MULTI_TURN_CHAT_COUNT = 5;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringTemplate;

    public void saveChat(String username, String content, ChatType chatType) throws GlobalException {

        String key = "chat:" + username;
        double score = (double) System.currentTimeMillis();
        // JSON 생성
        Map<String, String> chatMap = Map.of(
                "status", chatType.name(),
                "content", content
        );

        try {
            String json = objectMapper.writeValueAsString(chatMap);
            stringTemplate.opsForZSet().add(key, json, score);
        } catch (JsonProcessingException e) {
            throw new GlobalException(REDIS_CHAT_STORE_ERROR);
        }
    }

    public List<String> findChat(String username) {
        return stringTemplate.opsForList().range(username, -MULTI_TURN_CHAT_COUNT, -1);
    }
}
