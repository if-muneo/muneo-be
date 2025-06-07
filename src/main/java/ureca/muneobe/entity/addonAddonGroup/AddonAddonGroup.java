package ureca.muneobe.entity.addonAddonGroup;

import jakarta.persistence.*;
import ureca.muneobe.entity.addon.Addon;
import ureca.muneobe.entity.addonGroup.AddonGroup;

@Entity
public class AddonAddonGroup {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_id")
    private Addon addon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;
}
