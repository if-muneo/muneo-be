package ureca.muneobe.entity.subscription;

import jakarta.persistence.*;
import org.sangyunpark.muneo_entity.entity.combinedGroup.CombinedGroup;
import org.sangyunpark.muneo_entity.entity.common.BaseEntity;
import org.sangyunpark.muneo_entity.entity.mplan.Mplan;
import org.sangyunpark.muneo_entity.entity.user.User;

@Entity
public class Subscription extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_id")
    private Mplan mplan;

    private int fee;

    @ManyToOne
    @JoinColumn(name = "combined_group_id")
    private CombinedGroup combinedGroup;
}
