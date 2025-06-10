package ureca.muneobe.common.addon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addon.entity.DefaultAddon;
import ureca.muneobe.common.chat.entity.AddonType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAddonResponse {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private AddonType addonType;

    public static DefaultAddonResponse from(final DefaultAddon defaultAddon){
        return DefaultAddonResponse.builder()
                .id(defaultAddon.getId())
                .name(defaultAddon.getName())
                .description(defaultAddon.getDescription())
                .price(defaultAddon.getPrice())
                .addonType(defaultAddon.getAddonType())
                .build();
    }
}
