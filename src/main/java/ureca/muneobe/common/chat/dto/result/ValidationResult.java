package ureca.muneobe.common.chat.dto.result;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidationResult {
    private final boolean isValid;
    private final String message;
    private final String originalMessage;
}