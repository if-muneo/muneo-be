package ureca.muneobe.common.chat.service.rdb.input;

import lombok.Getter;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

@Getter
public class MplanCondition {
    private String name;
    private Range basicDataAmount;
    private Range sharingData;
    private Range monthlyPrice;
    private Range voiceCallVolume;
    private Boolean textMessage;
    private Qualification qualification;
    private DataType dataType;
    private MplanType mplanType;
}
