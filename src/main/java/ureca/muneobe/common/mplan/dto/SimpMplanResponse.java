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
public class SimpMplanResponse {
    private Long id;
    private String name;

    public static SimpMplanResponse from(final Mplan mplan){
        return SimpMplanResponse.builder()
                .id(mplan.getId())
                .name(mplan.getName())
                .build();
    }
}
