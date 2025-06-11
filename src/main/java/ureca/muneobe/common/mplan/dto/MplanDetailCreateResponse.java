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

    public static MplanDetailCreateResponse from(MplanDetail mplanDetail){
        return MplanDetailCreateResponse.builder()
                .id(mplanDetail.getId())
                .basicDataAmount(mplanDetail.getBasicDataAmount())
                .dailyData(mplanDetail.getDailyData())
                .sharingData(mplanDetail.getSharingData())
                .monthlyPrice(mplanDetail.getMonthlyPrice())
                .voiceCallVolume(mplanDetail.getVoiceCallVolume())
                .textMessage(mplanDetail.getTextMessage())
                .subDataSpeed(mplanDetail.getSubDataSpeed())
                .qualification(mplanDetail.getQualification())
                .mplanType(mplanDetail.getMplanType())
                .dataType(mplanDetail.getDataType())
                .build();
    }
}
