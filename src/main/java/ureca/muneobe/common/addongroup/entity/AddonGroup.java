package ureca.muneobe.common.addongroup.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addongroup.dto.request.AddonGroupCreateRequest;

import java.util.List;

@Entity
@Table(name = "addon_group")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddonGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "addon_group_name")
    private String addonGroupName;

    @OneToMany(mappedBy = "addonGroup")
    private List<Addon> addons;

    public static AddonGroup from(AddonGroupCreateRequest addonGroupCreateRequest){
        return AddonGroup.builder()
                .addonGroupName(addonGroupCreateRequest.getName())
                .build();
    }
}
