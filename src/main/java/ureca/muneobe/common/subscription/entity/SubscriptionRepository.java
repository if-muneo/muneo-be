package ureca.muneobe.common.subscription.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.auth.entity.Member;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByMember(Member member);
}
