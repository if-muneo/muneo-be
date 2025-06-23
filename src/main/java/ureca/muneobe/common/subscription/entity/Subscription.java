package ureca.muneobe.common.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.global.common.BaseEntity;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fee")
    private Integer fee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_id")
    private Mplan mplan;

    @Builder
    public Subscription(Member member, Mplan mplan, Integer fee) {
        this.member = member;
        this.mplan = mplan;
        this.fee = fee;
    }
}
