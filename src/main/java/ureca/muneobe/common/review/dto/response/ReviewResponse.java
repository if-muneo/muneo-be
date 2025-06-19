package ureca.muneobe.common.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.review.entity.Review;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private MemberResponse memberResponse;

    public static ReviewResponse from(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .deletedAt(review.getDeletedAt())
                .memberResponse(MemberResponse.from(review.getMember()))
                .build();

    }
}
