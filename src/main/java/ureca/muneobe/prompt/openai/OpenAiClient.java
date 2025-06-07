package ureca.muneobe.prompt.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ureca.muneobe.config.openai.OpenAiFirstPrompt;
import ureca.muneobe.config.openai.OpenAiSecondPrompt;
import ureca.muneobe.prompt.openai.dto.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient openAiWebClient;
    private final OpenAiFirstPrompt firstPrompt;
    private final OpenAiSecondPrompt secondPrompt;

    /**
     * 1차 프롬프트 호출
     * Builer 패턴으로 바꾸기
     */
    public Mono<IntentJson> callFirstPrompt(String userMassage, List<String> chatLog) {
        List<Message> messages = List.of(
                new Message("system", firstPrompt.getPrompt()),
                new Message("user", userMassage)
        );

        OpenAiRequest request = new OpenAiRequest(
                firstPrompt.getModel(),
                messages,
                firstPrompt.getTemperature(),
                firstPrompt.getMaxTokens()
        );

        log.info(userMassage);

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getIntentJson)
                .map(content -> {
                    try {
                        return new ObjectMapper().readValue(content, IntentJson.class);
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 파싱 실패", e);
                    }
                }
               )
                .doOnNext(resp -> log.info("1차 응답 IntentJson: {}", resp))
                .onErrorResume(e -> {
                    log.error("1차 프롬프트 에러", e);
                    return Mono.error(e); // 에러
                });
    }

    public Mono<String> callSecondPrompt(String userMessage, String dbData) {
        List<Message> messages = List.of(
                new Message("system", secondPrompt.getPrompt()),
                new Message("user", "사용자 질문: " + userMessage),
                new Message("user", "참고 데이터: " + dbData)
        );

        OpenAiRequest request = new OpenAiRequest(
                secondPrompt.getModel(),
                messages,
                secondPrompt.getTemperature(),
                secondPrompt.getMaxTokens()
        );

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getIntentJson)
                .doOnNext(resp -> log.info("2차 응답: {}", resp))
                .onErrorResume(e -> {
                    log.error("2차 프롬프트 에러", e);
                    return Mono.just("{\"router\": \"NONE\"}");
                });
    }
}
