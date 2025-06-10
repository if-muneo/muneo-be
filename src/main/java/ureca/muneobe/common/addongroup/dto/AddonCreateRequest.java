package ureca.muneobe.common.addongroup.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.chat.entity.AddonType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddonCreateRequest {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private AddonType addonType;
}
