package ureca.muneobe.common.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.global.common.BaseEntity;

@Entity
@Table(name = "chat")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChatType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Chat of(String content, ChatType status, Member member){
        return Chat.builder()
                .content(content)
                .status(status)
                .member(member)
                .build();
    }
}
