package ureca.muneobe.common.chat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.chat.dto.result.ValidationResult;
import ureca.muneobe.common.slang.service.SlangFilterService;

@Component
@RequiredArgsConstructor
public class SlangValidator {

    private final SlangFilterService slangFilterService;

    /**
     * 금칙어 포함 여부 검증
     */
    public boolean containsSlang(String message) {
        return slangFilterService.filteringSlang(message);
    }

    /**
     * 메시지 검증 결과 객체로 반환
     */
    public ValidationResult validateMessage(String message) {
        boolean hasSlang = containsSlang(message);
        return ValidationResult.builder()
                .isValid(!hasSlang)
                .message(hasSlang ? "부적절한 요청입니다." : "")
                .originalMessage(message)
                .build();
    }
}