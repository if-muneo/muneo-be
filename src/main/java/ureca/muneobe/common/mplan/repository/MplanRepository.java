package ureca.muneobe.common.mplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.mplan.entity.Mplan;

public interface MplanRepository extends JpaRepository<Mplan, Long> {
}
