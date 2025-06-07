package ureca.muneobe.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseBody<T> {
    private final int statusCode;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)  //직렬화 시 null이면, 아예 json에서 필드를 빼버림
    private Integer errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseBody(T data) {
        this.statusCode = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }

    public ResponseBody(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatus().value();
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static <T> ResponseBody<T> success(T data) {
        return new ResponseBody<>(data);
    }

    public static <T> ResponseBody<T> error(ErrorCode errorCode) {
        return new ResponseBody<>(errorCode);
    }
}
