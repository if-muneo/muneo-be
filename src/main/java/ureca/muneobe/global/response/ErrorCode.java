package ureca.muneobe.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //글로벌 에러 1000번 때
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 1000, "잘못된 입력값입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 1004, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 1005, "권한이 없는 사용자입니다."),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1006, "DB 문제가 발생했습니다."),
    UNSUPPORTED_REQUEST(HttpStatus.NOT_FOUND, 1007, "존재하지 않는 요청입니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, 1008, "지원하는 파일 형식이 아닙니다."),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1009, "서버 시스템 문제가 발생했습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, 1010, "파일을 찾을 수 없습니다."),

    //인증 에러 2000번 때

    //벡터 팀 에러 3000번 때
    TOKENIZATION_FAILED(HttpStatus.BAD_REQUEST, 3001, "토큰화가 되지 않았습니다."),
    NO_SIMILAR_DATA(HttpStatus.NOT_FOUND, 3002, "유사한 데이터를 조회할 수 없습니다."),

    //RDB 팀 에러 4000번 때

    //프롬프트 팀 에러 5000번 때
    JSON_PARSING_ERROR(HttpStatus.NOT_FOUND, 5001, "JSON 파싱을 실패했습니다."),
    FIRST_PROMPT_ERROR(HttpStatus.NOT_FOUND, 5002, "1차 프롬프트 에러입니다."),
    SECOND_PROMPT_ERROR(HttpStatus.NOT_FOUND, 5002, "2차 프롬프트 에러입니다."),
    CHAT_RESPONSE_ERROR(HttpStatus.NOT_FOUND, 5003, "채팅 응답 생성 중 오류가 발생했습니다."),

    // REDIS 에러 6000번 때
    REDIS_CHAT_STORE_ERROR(HttpStatus.NOT_FOUND, 6001, "대화 내용 redis 저장 중 요류가 발생했습니다."),
    REDIS_CHAT_PARSE_ERROR(HttpStatus.NOT_FOUND, 6001, "대화 내용 DB 저장 중 요류가 발생했습니다."),

    //demo 에러
    DEMO_ERROR(HttpStatus.BAD_REQUEST, 9999, "데모 에러입니다."),
    //CRUD 에러 6000번 때
    NO_MPLAN(HttpStatus.BAD_REQUEST, 6000, "해당 id에 해당하는 mplan이 없습니다."),
    NOT_YOUR_REVIEW(HttpStatus.BAD_REQUEST, 6001, "당신의 리뷰가 아닙니다."),
    REVIEW_CONTENT_TOO_SHORT(HttpStatus.BAD_REQUEST, 6002, "글자 수는 10자 이상 작성해야 합니다."),
    NOT_ELIGIBLE_REVIEW_USER(HttpStatus.FORBIDDEN, 6002, "가입된 요금제가 아닙니다."),

    // 금칙어 에러 7000번대
    SLANG_WORDS_REQUIRED(HttpStatus.BAD_REQUEST, 7000, "단어를 입력해야 합니다."),
    SLANG_WORDS_DETECTED(HttpStatus.BAD_REQUEST, 7001, "금칙어가 포함되어 있습니다."),
    ;

    private final HttpStatus status;
    private final int code;
    private final String message;
}
