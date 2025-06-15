package ureca.muneobe.common.mplan.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.mplan.entity.MplanDetail;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetailsResponse {
    private Page<MplanDetailResponse> mplanDetailsResponse;

    public static MplanDetailsResponse from(final Page<MplanDetail> mplanDetails) {
        return MplanDetailsResponse.builder()
                .mplanDetailsResponse(mplanDetails.map(MplanDetailResponse::from))
                .build();
    }
}
