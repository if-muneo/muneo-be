package ureca.muneobe.common.addon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAddonCreateResponse {
    private Long id;

    public static DefaultAddonCreateResponse from(Long id) {
        return DefaultAddonCreateResponse.builder()
                .id(id)
                .build();
    }
}
