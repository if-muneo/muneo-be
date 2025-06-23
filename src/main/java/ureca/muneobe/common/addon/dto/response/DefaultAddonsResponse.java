package ureca.muneobe.common.addon.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.addon.entity.DefaultAddon;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAddonsResponse {
    private Page<DefaultAddonResponse> addonsResponse;

    public static DefaultAddonsResponse from(Page<DefaultAddon> addons){
        return DefaultAddonsResponse.builder()
                .addonsResponse(addons.map(DefaultAddonResponse::from))
                .build();
    }
}
