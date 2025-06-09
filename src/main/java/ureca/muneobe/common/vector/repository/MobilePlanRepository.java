package ureca.muneobe.common.vector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.vector.entity.MobilePlan;

import java.util.List;

public interface MobilePlanRepository extends JpaRepository<MobilePlan, Long> {
    List<MobilePlan> findByEmbeddingIsNull();
}