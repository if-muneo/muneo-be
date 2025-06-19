package ureca.muneobe.common.review.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.review.dto.request.ReviewCreateRequest;
import ureca.muneobe.common.review.dto.request.ReviewDeleteRequest;
import ureca.muneobe.common.review.dto.response.ReviewCreateResponse;
import ureca.muneobe.common.review.dto.response.ReviewDeleteResponse;
import ureca.muneobe.common.review.dto.response.ReviewsResponse;
import ureca.muneobe.common.review.service.ReviewService;
import ureca.muneobe.common.slang.service.SlangFilterService;
import ureca.muneobe.common.subscription.repository.SubScriptionRepository;
import ureca.muneobe.global.response.ResponseBody;

import java.util.List;

import static org.springframework.data.redis.connection.ReactiveStreamCommands.AddStreamRecord.body;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewsResponse>> readReview(
            @PathVariable Long mplanId
    ) {
        return ResponseEntity.ok().body(ResponseBody.success(reviewService.findAll(mplanId)));
    }

    /**
     * 해당 모바일 요금제 사용자만 리뷰를 달 수 있다.
     * 리뷰를 달았을 때 금칙어가 포함되어 있다면 리뷰를 작성할 수 없다.
     * 리뷰가 작성하면 201를 보낸다.
     * 10글자 이상 작성해야 한다.
     */
    @PostMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewCreateResponse>> createReview(
            @PathVariable(name = "mplanId") Long mplanId,
            @RequestBody ReviewCreateRequest reviewCreateRequest,
            HttpSession httpSession
    ) {
        return ResponseEntity.ok().body(
                ResponseBody.success(reviewService.create(mplanId, reviewCreateRequest, SessionUtil.getLoginMember(httpSession).getId())));
    }

    @DeleteMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewDeleteResponse>> deleteReview(
            @RequestBody ReviewDeleteRequest reviewDeleteRequest,
            HttpSession httpSession
    ) {
        return ResponseEntity.ok().body(
                ResponseBody.success(reviewService.delete(reviewDeleteRequest, SessionUtil.getLoginMember(httpSession).getId())));
    }
}
