package ureca.muneobe.entity.review;

import jakarta.persistence.*;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.mplan.Mplan;
import ureca.muneobe.entity.user.User;

@Entity
public class Review extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_id")
    private Mplan mplan;
}
