package ureca.muneobe.common.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import ureca.muneobe.common.addongroup.dto.AddonGroupCreateRequest;

@Entity
@Table(name = "addon_group")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
