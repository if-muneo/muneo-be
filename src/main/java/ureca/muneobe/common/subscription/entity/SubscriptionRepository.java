package ureca.muneobe.common.subscription.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ureca.muneobe.common.auth.entity.Member;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByMember(Member member);

    @Query("SELECT s FROM Subscription s " +
            "LEFT JOIN FETCH s.mplan mp " +
            "LEFT JOIN FETCH mp.mplanDetail " +
            "LEFT JOIN FETCH mp.addonGroup " +
            "WHERE s.member.id = :memberId")
    Optional<Subscription> findByMemberIdWithMplan(@Param("memberId") Long memberId);
}
