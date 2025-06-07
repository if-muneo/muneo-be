package ureca.muneobe.global.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ResponseBody;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseBody<Void>> handleException(GlobalException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
            .body(ResponseBody.error(e.getErrorCode()));
    }
}
