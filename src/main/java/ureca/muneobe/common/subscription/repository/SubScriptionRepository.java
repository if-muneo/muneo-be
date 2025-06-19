package ureca.muneobe.common.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.subscription.entity.Subscription;

import java.util.List;

@Repository
public interface SubScriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByMember_Id(Long memberId);
    boolean existsByMemberIdAndMplanId(Long memberId, Long mplanId);
}
