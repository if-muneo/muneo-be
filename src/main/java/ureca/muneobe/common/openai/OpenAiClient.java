package ureca.muneobe.common.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.chat.config.openai.OpenAiFirstPrompt;
import ureca.muneobe.common.chat.config.openai.OpenAiSecondPrompt;
import ureca.muneobe.common.chat.service.MetaData;
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

    private final String DONE = "[DONE]";

    private final WebClient openAiWebClient;
    private final OpenAiFirstPrompt firstPrompt;
    private final OpenAiSecondPrompt secondPrompt;
    private final ObjectMapper objectMapper;

    /**
     * 1차 프롬프트 호출
     */
    public Mono<FirstPromptResponse> callFirstPrompt(MetaData metaData) {
        List<Message> messages = List.of(
                Message.from("system", firstPrompt.getPrompt() + " 이전 대화기록 " + metaData.getChatLog()),
                Message.from("user", metaData.getChatRequest().getContent())
        );

        OpenAiRequest request = OpenAiRequest.of(firstPrompt.getModel(), messages, firstPrompt.getTemperature(), firstPrompt.getMaxTokens(),false);

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
    @Transactional(readOnly = true)
    public <T> Flux<String> callSecondPrompt(List<T> dbData, MetaData metaData) {
        List<Message> messages = List.of(
                Message.from("system", new StringBuilder().append(secondPrompt.getPrompt()).append("\n")
                        .append("활용 데이터: ").append(dbData).append("\n")
                        .append("이전 대화 기록: ").append(metaData.getChatLog()).append("\n")
                        .append("유저 정보: ")
                                .append("\t").append("가입 요금제 이름: ").append(metaData.getMemberInfoMeta().getMplanName()).append("\n")
                                .append("\t").append("가입 요금제 정보: ").append(metaData.getMemberInfoMeta().getMplanDetailStr()).append("\n")
                                .append("\t").append("부가서비스 정보: ").append(metaData.getMemberInfoMeta().getAddonGroupStr()).append("\n")
                                .append("\t").append("이번달 데이터 사용량: ").append(metaData.getMemberInfoMeta().getUseAmount()).append("MB \n")
                                .toString()),
                Message.from("user", "사용자 질문: " + metaData.getChatRequest().getContent())
        );

        OpenAiRequest request = OpenAiRequest.of(secondPrompt.getModel(), messages, secondPrompt.getTemperature(), secondPrompt.getMaxTokens(),true);

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::extractDeltaContent)
                .takeUntil((DONE)::equals)
                .filter(line -> !line.isBlank() && !line.equals("[DONE]"));
    }

    private String extractDeltaContent(String line) {
        if (line == null || line.isBlank()) return "";

        if (DONE.equals(line.trim())) {
            return DONE;
        }

        try {
            JsonNode json = objectMapper.readTree(line); // 바로 JSON 파싱 시도
            return json.path("choices").get(0).path("delta").path("content").asText("");
        } catch (Exception e) {
            log.error("JSON 파싱 실패: {}", line);
            return "";
        }
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
