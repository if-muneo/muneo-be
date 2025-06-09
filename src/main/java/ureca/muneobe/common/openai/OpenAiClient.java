package ureca.muneobe.common.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.config.openai.OpenAiFirstPrompt;
import ureca.muneobe.common.chat.config.openai.OpenAiSecondPrompt;
import ureca.muneobe.common.openai.dto.IntentJson;
import ureca.muneobe.common.openai.dto.Message;
import ureca.muneobe.common.openai.dto.OpenAiRequest;
import ureca.muneobe.common.openai.dto.OpenAiResponse;

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
     */
    public Mono<IntentJson> callFirstPrompt(String userMassage, List<String> chatLog) {
        List<Message> messages = List.of(
                Message.from("system", firstPrompt.getPrompt()),
                Message.from("user", userMassage)
        );

        OpenAiRequest request = OpenAiRequest.of(firstPrompt.getModel(), messages, firstPrompt.getTemperature(), firstPrompt.getMaxTokens());

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
                    return Mono.error(e);
                });
    }

    public Mono<String> callSecondPrompt(String userMessage, String dbData) {
        List<Message> messages = List.of(
                Message.from("system", secondPrompt.getPrompt()),
                Message.from("user", "사용자 질문: " + userMessage),
                Message.from("user", "참고 데이터: " + dbData)
        );

        OpenAiRequest request = OpenAiRequest.of(secondPrompt.getModel(), messages, secondPrompt.getTemperature(), secondPrompt.getMaxTokens());

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getIntentJson)
                .doOnNext(resp -> log.info("2차 응답: {}", resp))
                .onErrorResume(e -> {
                    log.error("2차 프롬프트 에러", e);
                    return Mono.error(e);
                });
    }
}
