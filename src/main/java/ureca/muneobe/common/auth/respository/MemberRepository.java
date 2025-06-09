package ureca.muneobe.common.auth.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.auth.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByName(String username);

}
