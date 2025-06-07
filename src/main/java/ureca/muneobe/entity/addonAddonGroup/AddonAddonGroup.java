package ureca.muneobe.entity.addonAddonGroup;

import jakarta.persistence.*;
import org.sangyunpark.muneo_entity.entity.addon.Addon;
import org.sangyunpark.muneo_entity.entity.addonGroup.AddonGroup;

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
