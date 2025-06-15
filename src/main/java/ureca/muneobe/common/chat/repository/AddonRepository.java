package ureca.muneobe.common.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

public interface AddonRepository extends JpaRepository<Addon, Long> {
    List<Addon> findAddonsByAddonGroup(AddonGroup addonGroup);
}
