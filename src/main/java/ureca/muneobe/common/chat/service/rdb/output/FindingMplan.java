package ureca.muneobe.common.chat.service.rdb.output;

import java.util.List;

import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

public class FindingMplan {
    private String name;
    private Integer basicDataAmount;
    private Integer dailyData;
    private Integer sharingData;
    private Integer monthlyPrice;
    private Integer voiceCallVolume;
    private Boolean textMessage;
    private Integer subDataSpeed;
    private Qualification qualification;
    private DataType dataType;
    private MplanType mplanType;
    private List<FindingAddon> findingAddons;
}
