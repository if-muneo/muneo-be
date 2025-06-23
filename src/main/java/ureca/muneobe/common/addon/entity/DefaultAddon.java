package ureca.muneobe.common.addon.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.addon.dto.request.DefaultAddonCreateRequest;

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
