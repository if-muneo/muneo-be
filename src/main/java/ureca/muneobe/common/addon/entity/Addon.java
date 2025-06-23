package ureca.muneobe.common.addon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

@Entity
@Table(name = "addon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static Addon of(DefaultAddon defaultAddon, AddonGroup addonGroup){
        return Addon.builder()
                .name(defaultAddon.getName())
                .description(defaultAddon.getDescription())
                .price(defaultAddon.getPrice())
                .addonType(defaultAddon.getAddonType())
                .addonGroup(addonGroup)
                .build();
    }
}
