package ureca.muneobe.common.mplan.dto.response;

import lombok.*;
import ureca.muneobe.common.mplan.entity.MplanDetail;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetailCreateResponse {
    private Long id;

    public static MplanDetailCreateResponse from(final MplanDetail mplanDetail){
        return MplanDetailCreateResponse.builder()
                .id(mplanDetail.getId())
                .build();
    }
}
