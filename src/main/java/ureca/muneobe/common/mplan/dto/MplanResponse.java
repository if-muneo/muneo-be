package ureca.muneobe.common.mplan.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.entity.Addon;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.Mplan;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;

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
    private List<AddonGroupResponse> addonGroupsResponse;

    public static MplanResponse from(final Mplan mplan){
        return MplanResponse.builder()
                .id(mplan.getId())
                .name(mplan.getName())
                .build();
    }
}
