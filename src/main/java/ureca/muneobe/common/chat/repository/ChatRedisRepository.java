package ureca.muneobe.common.chat.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.service.MemberService;
import ureca.muneobe.common.chat.entity.Chat;
import ureca.muneobe.common.chat.entity.ChatType;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ureca.muneobe.global.response.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {

    private static final int MULTI_TURN_CHAT_COUNT = 5;

    private final StringRedisTemplate stringTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 대화 저장
     * username - score - value(statue, content)
     */
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

    /**
     * 멀티턴을 위한 대화 가져오기
     */
    public List<String> findChat(String username) {
        String key = "chat:" + username;

        Set<String> result = stringTemplate.opsForZSet()
                .reverseRange(key, 0, MULTI_TURN_CHAT_COUNT);

        return (result == null || result.isEmpty()) ? List.of() : new ArrayList<>(result);
    }

    /**
     * 대화 전체 가져오기
     */
    public List<Chat> extractChatLogsFromRedis(Member member) {
        String username = member.getName();
        String key = "chat:" + username;
        Set<String> chatSet = stringTemplate.opsForZSet().range(key, 0, -1);
        if (chatSet == null || chatSet.isEmpty()) return List.of();

        List<Chat> chatLogs = new ArrayList<>();
        for (String json : chatSet) {
            try {
                Map<String, String> chatMap = objectMapper.readValue(json, Map.class);
                String content = chatMap.get("content");
                ChatType status = ChatType.valueOf(chatMap.get("status"));
                chatLogs.add(Chat.of(content, status, member));
            } catch (JsonProcessingException e) {
                throw new GlobalException(REDIS_CHAT_PARSE_ERROR);
            }
        }
        return chatLogs;
    }

    /**
     * Redis 비우기
     */
    public void clearChat(String username) {
        stringTemplate.delete("chat:" + username);
    }




}
