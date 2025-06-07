package ureca.muneobe.entity.review;

import jakarta.persistence.*;
import org.sangyunpark.muneo_entity.entity.common.BaseEntity;
import org.sangyunpark.muneo_entity.entity.mplan.Mplan;
import org.sangyunpark.muneo_entity.entity.user.User;

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
