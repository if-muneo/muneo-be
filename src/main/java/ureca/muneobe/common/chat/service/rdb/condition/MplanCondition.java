package ureca.muneobe.common.chat.service.rdb.condition;

import lombok.Getter;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

@Getter
public class MplanCondition {
    private Number price;
    private Number basicDataAmount;
    private Number sharingData;
    private Number monthlyPrice;
    private Number voiceCallVolume;
    private Boolean textMessage;
    private Qualification qualification;
    private MplanType mplanType;
    private DataType dataType;
}
