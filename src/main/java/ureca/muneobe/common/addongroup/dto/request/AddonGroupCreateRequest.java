package ureca.muneobe.common.addongroup.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonGroupCreateRequest {
    private String name;
    private List<AddonCreateRequest> addonsCreateRequest;
}
