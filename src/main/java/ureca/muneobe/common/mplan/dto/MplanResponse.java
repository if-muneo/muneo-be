package ureca.muneobe.common.mplan.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.entity.Mplan;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanResponse {
    private Long id;
    private String name;

    public static MplanResponse from(final Mplan mplan){
        return MplanResponse.builder()
                .id(mplan.getId())
                .name(mplan.getName())
                .build();
    }
}
