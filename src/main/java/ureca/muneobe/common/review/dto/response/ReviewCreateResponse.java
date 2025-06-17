package ureca.muneobe.common.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.review.entity.Review;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateResponse {
    private Long id;

    public static ReviewCreateResponse from(Review review){
        return ReviewCreateResponse.builder()
                .id(review.getId())
                .build();
    }
}
