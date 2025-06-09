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
public class SimpMplansResponse {
    private Page<SimpMplanResponse> mplanResponses;

    public static SimpMplansResponse from(Page<SimpMplanResponse> simpMplansResponse){
        return SimpMplansResponse.builder()
                .mplanResponses(simpMplansResponse)
                .build();
    }
}
