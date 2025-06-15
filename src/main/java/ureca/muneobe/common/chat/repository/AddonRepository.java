package ureca.muneobe.common.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.chat.entity.Addon;
import ureca.muneobe.common.chat.entity.AddonGroup;

public interface AddonRepository extends JpaRepository<Addon, Long> {
    List<Addon> findAddonsByAddonGroup(AddonGroup addonGroup);
}
