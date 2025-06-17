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
import ureca.muneobe.global.response.ResponseBody;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewsResponse>> readReview(
            @PathVariable Long mplanId
    ){
        return ResponseEntity.ok().body(ResponseBody.success(reviewService.findAll(mplanId)));
    }

    @PostMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewCreateResponse>> createReview(
            @PathVariable(name = "mplanId") Long mplanId,
            @RequestBody ReviewCreateRequest reviewCreateRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(
                ResponseBody.success(reviewService.create(mplanId, reviewCreateRequest, SessionUtil.getLoginMember(httpSession).getId())));
    }

    @DeleteMapping("/v1/{mplanId}/review")
    public ResponseEntity<ResponseBody<ReviewDeleteResponse>> deleteReview(
            @RequestBody ReviewDeleteRequest reviewDeleteRequest,
            HttpSession httpSession
    ){
        return ResponseEntity.ok().body(
                ResponseBody.success(reviewService.delete(reviewDeleteRequest, SessionUtil.getLoginMember(httpSession).getId())));
    }
}
