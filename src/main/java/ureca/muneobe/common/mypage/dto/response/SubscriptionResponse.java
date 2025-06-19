package ureca.muneobe.common.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.subscription.entity.Subscription;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private Integer fee;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private MplanResponse mplanResponse;

    public static SubscriptionResponse from(Subscription subscription){
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .fee(subscription.getFee())
                .createdAt(subscription.getCreatedAt())
                .deletedAt(subscription.getDeletedAt())
                .updatedAt(subscription.getUpdatedAt())
                .mplanResponse(MplanResponse.from(subscription.getMplan()))
                .build();
    }
}
