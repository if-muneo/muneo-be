package ureca.muneobe.entity.combinedGroup;

import jakarta.persistence.*;
import ureca.muneobe.entity.combined.Combine;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CombinedGroup extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combine_id")
    private Combine combine;

    @OneToMany(mappedBy = "combinedGroup")
    private List<Subscription> subscriptions = new ArrayList<>();
}
