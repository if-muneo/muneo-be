package ureca.muneobe.common.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.review.entity.Review;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsResponse {
    private List<ReviewResponse> reviewsResponse;

    public static ReviewsResponse from(final List<Review> reviews){
        return ReviewsResponse.builder()
                .reviewsResponse(reviews.stream().map(ReviewResponse::from).toList())
                .build();
    }
}
