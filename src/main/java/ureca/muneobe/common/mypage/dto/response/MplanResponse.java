package ureca.muneobe.common.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ureca.muneobe.common.mplan.entity.Mplan;

@Getter
@Builder
@AllArgsConstructor
public class MplanResponse {
    private Long id;
    private String name;

    public static MplanResponse from(Mplan mplan){
        return MplanResponse.builder()
                .id(mplan.getId())
                .name(mplan.getName())
                .build();

    }
}
