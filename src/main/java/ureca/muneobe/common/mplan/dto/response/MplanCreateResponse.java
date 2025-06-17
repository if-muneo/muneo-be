package ureca.muneobe.common.mplan.dto.response;

import lombok.*;
import ureca.muneobe.common.mplan.entity.Mplan;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanCreateResponse {
    private Long id;

    public static MplanCreateResponse from(final Mplan mplan) {
        return MplanCreateResponse.builder()
                .id(mplan.getId())
                .build();
    }
}