package ureca.muneobe.common.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.chat.config.openai.OpenAiFirstPrompt;
import ureca.muneobe.common.chat.config.openai.OpenAiSecondPrompt;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.dto.result.PreProcessResult;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanDetail;
import ureca.muneobe.common.openai.dto.Message;
import ureca.muneobe.common.openai.dto.OpenAiRequest;
import ureca.muneobe.common.openai.dto.OpenAiResponse;
import ureca.muneobe.common.openai.dto.router.FirstPromptResponse;
import ureca.muneobe.common.subscription.entity.Subscription;
import ureca.muneobe.common.subscription.entity.SubscriptionRepository;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;
import java.util.Optional;

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
    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    /**
     * 1차 프롬프트 호출
     */
    public Mono<FirstPromptResult> callFirstPrompt(PreProcessResult preProcessResult) {
        List<Message> messages = List.of(
                Message.from("system", firstPrompt.getPrompt() + " 이전 대화기록 " + preProcessResult.getChatLog()),
                Message.from("user", preProcessResult.getMessage())
        );

        OpenAiRequest request = OpenAiRequest.of(firstPrompt.getModel(), messages, firstPrompt.getTemperature(), firstPrompt.getMaxTokens(),false);

        return openAiWebClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getIntentJson)
                .map(this::parseIntentJson)
                .map(firstPromptResponse -> FirstPromptResult.of(firstPromptResponse, preProcessResult.getMessage(), preProcessResult.getChatLog()))
                .doOnNext(resp -> log.info("1차 응답 IntentJson: {}", resp))
                .onErrorResume(this::handlePromptError);
    }

    /**
     * 2차 프롬프트 호출
     */
    @Transactional(readOnly = true)
    public <T> Flux<String> callSecondPrompt(String userMessage, List<T> dbData, List<String> chatLog, String memberName) {
        Member member = memberRepository.findByName(memberName).get();
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByMember(member);
        String mplanName       = optionalSubscription
                .map(sub -> sub.getMplan().getName())
                .orElse("");
        String mplanDetailStr  = optionalSubscription
                .map(sub -> sub.getMplan().getMplanDetail().toString())
                .orElse("");
        String addonGroupStr   = optionalSubscription
                .map(sub -> sub.getMplan().getAddonGroup().toString())
                .orElse("");

        List<Message> messages = List.of(
                Message.from("system", new StringBuilder().append(secondPrompt.getPrompt()).append("\n")
                        .append("활용 데이터: ").append(dbData).append("\n")
                        .append("이전 대화 기록: ").append(chatLog).append("\n")
                        .append("유저 정보: ")
                                .append("\t").append("가입 요금제 이름: ").append(mplanName).append("\n")
                                .append("\t").append("가입 요금제 정보: ").append(mplanDetailStr).append("\n")
                                .append("\t").append("부가서비스 정보: ").append(addonGroupStr).append("\n")
                                .toString()),
                Message.from("user", "사용자 질문: " + userMessage)
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
    private Mono<FirstPromptResult> handlePromptError(Throwable e) {
        // json 에러일 경우
        if (e instanceof GlobalException) {
            return Mono.error(e);
        }
        return Mono.error(new GlobalException(FIRST_PROMPT_ERROR));
    }
}
