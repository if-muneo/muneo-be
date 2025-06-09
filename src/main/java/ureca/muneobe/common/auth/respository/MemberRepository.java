package ureca.muneobe.common.auth.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.auth.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMembername(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByMembername(String username);

}
