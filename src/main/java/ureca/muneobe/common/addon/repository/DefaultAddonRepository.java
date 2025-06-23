package ureca.muneobe.common.addon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.addon.entity.DefaultAddon;

import java.util.Optional;


@Repository
public interface DefaultAddonRepository extends JpaRepository<DefaultAddon, Long> {
    Optional<DefaultAddon> findByName(String name);
}
