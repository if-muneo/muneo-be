package ureca.muneobe.common.mplan.dto;

import lombok.*;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetailCreateRequest {
    private Long id;
    private Integer basicDataAmount;
    private Integer dailyData;
    private Integer sharingData;
    private Integer monthlyPrice;
    private Integer voiceCallVolume;
    private Boolean textMessage;
    private Integer subDataSpeed;
    private Qualification qualification;
    private MplanType mplanType;
    private DataType dataType;
}
