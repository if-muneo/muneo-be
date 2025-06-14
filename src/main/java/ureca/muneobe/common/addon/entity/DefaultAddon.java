package ureca.muneobe.common.addon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addon.dto.request.DefaultAddonCreateRequest;
import ureca.muneobe.common.chat.entity.AddonType;

@Entity
@Table(name = "default_addon")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAddon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "addon_type")
    @Enumerated(EnumType.STRING)
    private AddonType addonType;

    public static DefaultAddon from(DefaultAddonCreateRequest defaultAddonCreateRequest){
        return DefaultAddon.builder()
                .name(defaultAddonCreateRequest.getName())
                .description(defaultAddonCreateRequest.getDescription())
                .price(defaultAddonCreateRequest.getPrice())
                .addonType(defaultAddonCreateRequest.getAddonType())
                .build();
    }
}
