package ureca.muneobe.common.chat.service.strategy.rdb.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.mplan.entity.DataType;
import ureca.muneobe.common.mplan.entity.MplanType;
import ureca.muneobe.common.mplan.entity.Qualification;

@Getter
@AllArgsConstructor
@Builder
public class MplanCondition {
    private String name;
    private Range monthlyPrice;
    private Range dailyData;
    private Range basicDataAmount;
    private Range sharingData;
    private Range voiceCallVolume;
    private Range subDataSpeed;
    private Boolean textMessage;
    private Qualification qualification;
    private DataType dataType;
    private MplanType mplanType;
}
