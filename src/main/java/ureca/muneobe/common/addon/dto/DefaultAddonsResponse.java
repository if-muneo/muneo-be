package ureca.muneobe.common.addon.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ureca.muneobe.common.addon.entity.DefaultAddon;
import ureca.muneobe.common.chat.entity.Addon;

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
