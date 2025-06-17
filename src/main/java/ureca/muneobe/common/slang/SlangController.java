package ureca.muneobe.common.slang;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ureca.muneobe.global.response.ErrorCode;
import ureca.muneobe.global.response.ResponseBody;
import ureca.muneobe.common.slang.dto.AddSlangRequest;
import ureca.muneobe.common.slang.dto.AddSlangResponse;
import ureca.muneobe.common.slang.dto.DeleteSlangRequest;
import ureca.muneobe.common.slang.dto.DeleteSlangResponse;
import ureca.muneobe.common.slang.service.SlangService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/slang")
public class SlangController {

    private final SlangService slangService;

    /**
     * 요청된 금칙어 목록을 추가 처리합니다.
     * <p>
     * - 요청 바디의 words 필드가 비어있거나 null일 경우 400 Bad Request를 반환합니다.
     * - slangService.addSlang을 통해 단어 추가를 시도하며, 실패한 단어 목록을 응답합니다.
     *
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseBody<AddSlangResponse>> addSlang(@RequestBody AddSlangRequest request) {
        List<String> words = request.getWords();
        if (words == null || words.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseBody.error(ErrorCode.SLANG_WORDS_REQUIRED));
        }

        List<String> failedList = slangService.addSlang(words);
        AddSlangResponse response = new AddSlangResponse(failedList);
        return ResponseEntity.ok(ResponseBody.success(response));
    }

    /**
     * 요청된 금칙어 목록을 삭제 처리합니다.
     * <p>
     * - 요청 바디의 words 필드가 비어있거나 null일 경우 400 Bad Request를 반환합니다.
     * - slangService.deleteSlang을 통해 단어 삭제를 시도하며, 실패한 단어 목록을 응답합니다.
     *
     * @param request
     * @return
     */
    @DeleteMapping
    public ResponseEntity<ResponseBody<DeleteSlangResponse>> removeSlang(@RequestBody DeleteSlangRequest request) {
        List<String> words = request.getWords();
        if (words == null || words.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseBody.error(ErrorCode.SLANG_WORDS_REQUIRED));
        }

        List<String> failedList = slangService.deleteSlang(words);
        DeleteSlangResponse response = new DeleteSlangResponse(failedList);
        return ResponseEntity.ok(ResponseBody.success(response));
    }
}
