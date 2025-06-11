package ureca.muneobe.common.mplan.dto;

import lombok.*;
import ureca.muneobe.common.chat.entity.Mplan;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanCreateResponse {
    private Long id;

    public static MplanCreateResponse from(Mplan mplan) {
        return MplanCreateResponse.builder()
                .id(mplan.getId())
                .build();
    }
}