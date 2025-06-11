package ureca.muneobe.common.mplan.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetailsResponse {
    private Page<MplanDetailResponse> mplanDetailsResponse;
}
