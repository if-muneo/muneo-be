package ureca.muneobe.common.mplan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.mplan.entity.Mplan;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MplansResponse {
    private Page<MplanResponse> mplansResponse;

    public static MplansResponse from(Page<Mplan> mplans){
        return MplansResponse.builder()
                .mplansResponse(mplans.map(MplanResponse::from))
                .build();
    }
}
