package ureca.muneobe.common.vector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.vector.entity.Fat;

import java.util.List;

public interface FatRepository extends JpaRepository<Fat, Long> {
    List<Fat> findByEmbeddingFalse();
}