package ureca.muneobe.common.mplan.dto.response;

import lombok.*;
import ureca.muneobe.common.mplan.entity.UnapplyMplan;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UnapplyMplanCreateResponse {
    private Long id;

    public static UnapplyMplanCreateResponse from(final UnapplyMplan unapplyMplan) {
        return UnapplyMplanCreateResponse.builder()
                .id(unapplyMplan.getId())
                .build();
    }
}
