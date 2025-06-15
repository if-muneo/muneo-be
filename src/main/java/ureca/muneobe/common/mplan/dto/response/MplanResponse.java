package ureca.muneobe.common.mplan.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.mplan.entity.DataType;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanType;
import ureca.muneobe.common.mplan.entity.Qualification;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanResponse {
    private Long id;
    private String name;
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
    private AddonGroupResponse addonGroupResponse;

    public static MplanResponse from(final Mplan mplan){
        return MplanResponse.builder()
                .id(mplan.getId())
                .name(mplan.getName())
                .basicDataAmount(mplan.getMplanDetail().getBasicDataAmount())
                .dailyData(mplan.getMplanDetail().getDailyData())
                .sharingData(mplan.getMplanDetail().getSharingData())
                .monthlyPrice(mplan.getMplanDetail().getMonthlyPrice())
                .voiceCallVolume(mplan.getMplanDetail().getVoiceCallVolume())
                .textMessage(mplan.getMplanDetail().getTextMessage())
                .subDataSpeed(mplan.getMplanDetail().getSubDataSpeed())
                .qualification(mplan.getMplanDetail().getQualification())
                .mplanType(mplan.getMplanDetail().getMplanType())
                .dataType(mplan.getMplanDetail().getDataType())
                .addonGroupResponse(AddonGroupResponse.from(mplan.getAddonGroup()))
                .build();
    }
}
