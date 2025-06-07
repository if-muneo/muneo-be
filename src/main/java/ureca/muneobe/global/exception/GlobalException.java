package ureca.muneobe.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ureca.muneobe.global.response.ErrorCode;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {
    private final ErrorCode errorCode;
}
