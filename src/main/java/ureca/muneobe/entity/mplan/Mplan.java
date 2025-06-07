package ureca.muneobe.entity.mplan;

import jakarta.persistence.*;
import ureca.muneobe.entity.addonGroup.AddonGroup;
import ureca.muneobe.entity.mplanDetail.MplanDetail;
import ureca.muneobe.entity.review.Review;
import ureca.muneobe.entity.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Mplan {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    @OneToMany(mappedBy = "mplan")
    List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "mplan")
    List<Subscription> subscriptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_detail_id")
    private MplanDetail mplanDetail;
}
