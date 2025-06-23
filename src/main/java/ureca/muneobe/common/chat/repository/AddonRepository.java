package ureca.muneobe.common.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addongroup.entity.AddonGroup;

public interface AddonRepository extends JpaRepository<Addon, Long> {
    List<Addon> findAddonsByAddonGroup(AddonGroup addonGroup);

    @Query("SELECT a.name FROM Addon a " +
            "JOIN a.addonGroup ag " +
            "JOIN Mplan mp ON mp.addonGroup = ag " +
            "WHERE mp.id = :mplanId")
    List<String> findAddonNamesByMplanId(@Param("mplanId") Long mplanId);
}
