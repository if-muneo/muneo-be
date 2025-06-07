package ureca.muneobe.entity.addonGroup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import ureca.muneobe.entity.addonAddonGroup.AddonAddonGroup;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AddonGroup {

    @Id @GeneratedValue
    private Long id;

    private String addonGroupName;

    @OneToMany(mappedBy = "addonGroup")
    private List<AddonAddonGroup> addonAddonGroups = new ArrayList<>();
}
