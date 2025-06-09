package ureca.muneobe.common.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntentJson {
        // 1차 프롬프트 결과 json dto..만들다 쥬금

    private String router;
}

