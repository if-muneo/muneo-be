package ureca.muneobe.common.review.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.global.common.BaseEntity;

@Entity
@Table(name = "Review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_id")
    private Mplan mplan;

    public static Review of(Member member, Mplan mplan, String content){
        return Review.builder()
                .content(content)
                .member(member)
                .mplan(mplan)
                .build();
    }
}
