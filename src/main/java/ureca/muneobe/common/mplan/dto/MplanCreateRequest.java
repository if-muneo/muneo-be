package ureca.muneobe.common.mplan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MplanCreateRequest {
    private String name;
    private Long mplanDetailId;
    private Long addonGroupId;
}
