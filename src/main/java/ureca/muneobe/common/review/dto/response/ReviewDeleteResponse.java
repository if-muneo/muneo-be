package ureca.muneobe.common.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.review.dto.request.ReviewDeleteRequest;
import ureca.muneobe.common.review.entity.Review;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDeleteResponse {
    private Long id;

    public static ReviewDeleteResponse from(Long id){
        return ReviewDeleteResponse.builder()
                .id(id)
                .build();
    }
}
