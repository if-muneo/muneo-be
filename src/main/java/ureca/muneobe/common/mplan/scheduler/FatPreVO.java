package ureca.muneobe.common.mplan.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanDetail;

import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class FatPreVO {
    //mplan
    private String name;

    //mplan_detail
    private Boolean textMessages;
    private Long basicDataAmount;
    private Long dailyData;
    private Long monthlyPrice;
    private Long price;
    private Long sharingData;
    private Long subDataSpeed;
    private Long voiceCallVolume;
    private String dataType;
    private String description;
    private String mplanType;
    private String qualification;

    //addon_group_name
    private String addon;

    //fatEmbedding
    private Boolean embedding;

    public static FatPreVO of(Mplan mplan, MplanDetail mplanDetail, String addonGroupName, String description, boolean isEmbedding) {
        return FatPreVO.builder()
                .name(mplan.getName())
                .basicDataAmount(Optional.ofNullable(mplanDetail.getBasicDataAmount()).map(Integer::longValue).orElse(null))
                .dailyData(Optional.ofNullable(mplanDetail.getDailyData()).map(Integer::longValue).orElse(null))
                .monthlyPrice(Optional.ofNullable(mplanDetail.getMonthlyPrice()).map(Integer::longValue).orElse(null))
                .price(Optional.ofNullable(mplanDetail.getMonthlyPrice()).map(Integer::longValue).orElse(null))
                .sharingData(Optional.ofNullable(mplanDetail.getSharingData()).map(Integer::longValue).orElse(null))
                .subDataSpeed(Optional.ofNullable(mplanDetail.getSubDataSpeed()).map(Integer::longValue).orElse(null))
                .voiceCallVolume(Optional.ofNullable(mplanDetail.getVoiceCallVolume()).map(Integer::longValue).orElse(null))
                .dataType(mplanDetail.getDataType().toString())
                .description(description)
                .mplanType(mplanDetail.getMplanType().toString())
                .qualification(mplanDetail.getQualification().toString())
                .addon(addonGroupName)
                .embedding(isEmbedding)
                .build();
    }
}
