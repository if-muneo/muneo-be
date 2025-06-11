package ureca.muneobe.common.mplan.dto;

import lombok.*;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanDetail;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetailCreateResponse {
    private Long id;

    public static MplanDetailCreateResponse from(MplanDetail mplanDetail){
        return MplanDetailCreateResponse.builder()
                .id(mplanDetail.getId())
                .build();
    }
}
