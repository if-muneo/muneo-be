package ureca.muneobe.common.addon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ureca.muneobe.common.addongroup.dto.request.AddonCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
