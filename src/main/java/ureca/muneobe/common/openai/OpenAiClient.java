package ureca.muneobe.common.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.config.openai.OpenAiFirstPrompt;
import ureca.muneobe.common.chat.config.openai.OpenAiSecondPrompt;
import ureca.muneobe.common.openai.dto.Message;
import ureca.muneobe.common.openai.dto.OpenAiRequest;
import ureca.muneobe.common.openai.dto.OpenAiResponse;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;

import static ureca.muneobe.global.response.ErrorCode.FIRST_PROMPT_ERROR;
import static ureca.muneobe.global.response.ErrorCode.JSON_PARSING_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient openAiWebClient;
    private final OpenAiFirstPrompt firstPrompt;
    private final OpenAiSecondPrompt secondPrompt;
    private final ObjectMapper objectMapper;

    /**
     * 1차 프롬프트 호출
     */
    public Mono<FirstPromptResponse> callFirstPrompt(String userMassage, List<String> chatLog) {
        List<Message> messages = List.of(
                Message.from("system", firstPrompt.getPrompt()),
                Message.from("user", userMassage)
        );

        OpenAiRequest request = OpenAiRequest.of(firstPrompt.getModel(), messages, firstPrompt.getTemperature(), firstPrompt.getMaxTokens());

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getIntentJson)
                .map(this::parseIntentJson)
                .doOnNext(resp -> log.info("1차 응답 IntentJson: {}", resp))
                .onErrorResume(this::handlePromptError);
    }

    /**
     * 2차 프롬프트 호출
     */
    public <T> Mono<String> callSecondPrompt(String userMessage, List<T> dbData) {
        List<Message> messages = List.of(
                Message.from("system", secondPrompt.getPrompt() + " 활용 데이터 " + dbData),
                Message.from("user", "사용자 질문: " + userMessage)
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

    /**
     * 1차 프롬프트 Json 파싱
     */
    private FirstPromptResponse parseIntentJson(String content) {
        try {
            return objectMapper.readValue(content, FirstPromptResponse.class);
        } catch (Exception e) {
            throw new GlobalException(JSON_PARSING_ERROR);
        }
    }

    /**
     * Error 핸들러
     */
    private Mono<FirstPromptResponse> handlePromptError(Throwable e) {
        // json 에러일 경우
        if (e instanceof GlobalException) {
            return Mono.error(e);
        }
        return Mono.error(new GlobalException(FIRST_PROMPT_ERROR));
    }
}
