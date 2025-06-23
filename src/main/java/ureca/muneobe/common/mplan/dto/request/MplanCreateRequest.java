package ureca.muneobe.common.mplan.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.mplan.entity.DataType;
import ureca.muneobe.common.mplan.entity.MplanType;
import ureca.muneobe.common.mplan.entity.Qualification;

@Getter
@NoArgsConstructor
public class MplanCreateRequest {
    //mplan
    private String name;

    //mplan_detail
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

    //addon_group
    private AddonGroupRequest addonGroupRequest;
}