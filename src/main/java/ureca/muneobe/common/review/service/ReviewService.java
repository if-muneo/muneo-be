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
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MplanRepository mplanRepository;
    private final MemberRepository memberRepository;

    public ReviewsResponse findAll(Long mplanId) {
        return getReviewsResponse(mplanId);
    }

    @Transactional
    public ReviewCreateResponse create(Long mplanId, ReviewCreateRequest reviewCreateRequest, Long memberId) {
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
}

