package ureca.muneobe.common.mplan.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplansResponse {
    private Page<MplanResponse> mplanResponses;

    public static MplansResponse from(Page<MplanResponse> simpMplansResponse){
        return MplansResponse.builder()
                .mplanResponses(simpMplansResponse)
                .build();
    }
}
