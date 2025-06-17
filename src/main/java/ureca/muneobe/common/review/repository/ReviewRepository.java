package ureca.muneobe.common.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMplan(Mplan mplan);
    Optional<Review> findByIdAndMember(Long id, Member member);
}
