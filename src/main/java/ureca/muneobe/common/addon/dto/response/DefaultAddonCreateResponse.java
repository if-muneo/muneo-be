package ureca.muneobe.common.addon.dto.response;

import lombok.*;

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
