package ureca.muneobe.common.vector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.vector.entity.FatEmbedding;
import java.util.List;

public interface FatEmbeddingRepository extends JpaRepository<FatEmbedding, Long> {
    List<FatEmbedding> findByFatId(Long fatId);
}