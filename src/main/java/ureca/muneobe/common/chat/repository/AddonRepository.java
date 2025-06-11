package ureca.muneobe.common.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.chat.entity.Addon;

public interface AddonRepository extends JpaRepository<Addon, Long> {
}
