package ureca.muneobe.common.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.addongroup.dto.AddonCreateRequest;

@Entity
@Table(name = "addon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Addon {
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

    @ManyToOne
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;

    public static Addon from(AddonCreateRequest addonCreateRequest){
        return Addon.builder()
                .name(addonCreateRequest.getName())
                .description(addonCreateRequest.getDescription())
                .price(addonCreateRequest.getPrice())
                .addonType(addonCreateRequest.getAddonType())
                .build();
    }

    public static Addon of(AddonCreateRequest addonCreateRequest, AddonGroup addonGroup){
        return Addon.builder()
                .name(addonCreateRequest.getName())
                .description(addonCreateRequest.getDescription())
                .price(addonCreateRequest.getPrice())
                .addonType(addonCreateRequest.getAddonType())
                .addonGroup(addonGroup)
                .build();
    }
}
