package ureca.muneobe.common.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.subscription.entity.Subscription;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionsResponse {
    private List<SubscriptionResponse> subscriptionsResponse;

    public static SubscriptionsResponse from(List<Subscription> subscriptions){
        return SubscriptionsResponse.builder()
                .subscriptionsResponse(subscriptions.stream().map(SubscriptionResponse::from).toList())
                .build();
    }
}
