package ureca.muneobe.entity.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.user.User;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private ChatStatus status;
}
