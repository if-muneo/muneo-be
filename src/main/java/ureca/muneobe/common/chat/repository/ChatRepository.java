package ureca.muneobe.common.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
