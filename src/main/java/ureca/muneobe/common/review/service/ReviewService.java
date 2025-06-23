package ureca.muneobe.common.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.common.review.dto.response.ReviewCreateResponse;
import ureca.muneobe.common.review.dto.request.ReviewCreateRequest;
import ureca.muneobe.common.review.dto.request.ReviewDeleteRequest;
import ureca.muneobe.common.review.dto.response.ReviewDeleteResponse;
import ureca.muneobe.common.review.dto.response.ReviewsResponse;
import ureca.muneobe.common.review.entity.Review;
import ureca.muneobe.common.review.repository.ReviewRepository;
import ureca.muneobe.common.slang.service.SlangFilterService;
import ureca.muneobe.common.subscription.repository.SubscriptionRepository;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MplanRepository mplanRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SlangFilterService slangFilterService;

    public ReviewsResponse findAll(Long mplanId) {
        return getReviewsResponse(mplanId);
    }

    @Transactional
    public ReviewCreateResponse create(Long mplanId, ReviewCreateRequest reviewCreateRequest, Long memberId) {
        // 1. 사용자 가입된 요금제인지 체크
        if (!isPossibleWriteReview(memberId, mplanId)) {
            throw new GlobalException(ErrorCode.NOT_ELIGIBLE_REVIEW_USER);
        }
        // 2. 10글자 이하이거나
        if (reviewCreateRequest.getContent() == null || reviewCreateRequest.getContent().length() < 10) {
            throw new GlobalException(ErrorCode.REVIEW_CONTENT_TOO_SHORT);
        }
        // 3. 금칙어 체크
        if (isContainsSlang(reviewCreateRequest.getContent())) {
            throw new GlobalException(ErrorCode.SLANG_WORDS_DETECTED);
        }
        Review review = reviewRepository.save(getReview(reviewCreateRequest, mplanId, memberId));
        return ReviewCreateResponse.from(review);
    }

    @Transactional
    public ReviewDeleteResponse delete(ReviewDeleteRequest reviewDeleteRequest, Long memberId) {
        if(!isYourReview(reviewDeleteRequest.getId(), memberId))
            throw new GlobalException(ErrorCode.NOT_YOUR_REVIEW);

        reviewRepository.delete(reviewRepository.getReferenceById(reviewDeleteRequest.getId()));
        return ReviewDeleteResponse.from(reviewDeleteRequest.getId());
    }

    private Review getReview(ReviewCreateRequest reviewCreateRequest, Long mplanId, Long memberId) {
        Mplan mplan = mplanRepository.getReferenceById(mplanId);
        Member member = memberRepository.getReferenceById(memberId);
        return Review.of(member, mplan, reviewCreateRequest.getContent());
    }

    private ReviewsResponse getReviewsResponse(Long mplanId) {
        List<Review> reviews = reviewRepository.findByMplan(mplanRepository.getReferenceById(mplanId));
        return ReviewsResponse.from(reviews);
    }

    private boolean isYourReview(Long reviewId, Long memberId) {
        return reviewRepository.findByIdAndMember(reviewId, memberRepository.getReferenceById(memberId)).isPresent();
    }

    private boolean isPossibleWriteReview(Long memberId, Long mplanId) {
        return subscriptionRepository.existsByMemberIdAndMplanId(memberId,mplanId);
    }

    private boolean isContainsSlang(String userMessage) {
        return slangFilterService.filteringSlang(userMessage);
    }
}

