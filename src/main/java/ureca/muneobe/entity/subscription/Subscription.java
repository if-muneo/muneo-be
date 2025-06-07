package ureca.muneobe.entity.subscription;

import jakarta.persistence.*;
import ureca.muneobe.entity.combinedGroup.CombinedGroup;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.mplan.Mplan;
import ureca.muneobe.entity.user.User;

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
