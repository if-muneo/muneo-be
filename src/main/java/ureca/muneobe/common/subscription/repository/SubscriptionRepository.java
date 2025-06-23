package ureca.muneobe.common.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.subscription.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByMember(Member member);

    @Query("SELECT s FROM Subscription s " +
            "LEFT JOIN FETCH s.mplan mp " +
            "LEFT JOIN FETCH mp.mplanDetail " +
            "LEFT JOIN FETCH mp.addonGroup " +
            "WHERE s.member.id = :memberId")
    Optional<Subscription> findByMemberIdWithMplan(@Param("memberId") Long memberId);
    List<Subscription> findAllByMember_Id(Long memberId);
    boolean existsByMemberIdAndMplanId(Long memberId, Long mplanId);
}
