package ureca.muneobe.common.mplan.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;
import ureca.muneobe.common.review.entity.Review;
import ureca.muneobe.common.subscription.entity.Subscription;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mplan")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Mplan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_detail_id")
    private MplanDetail mplanDetail;

    @OneToMany(mappedBy = "mplan")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "mplan")
    @Builder.Default
    private List<Subscription> subscriptions = new ArrayList<>();

    @Deprecated //스케쥴러 처리로 deprecated
    public static Mplan of(MplanCreateRequest mplanCreateRequest, AddonGroup addonGroup, MplanDetail mplanDetail){
        return Mplan.builder()
                .name(mplanCreateRequest.getName())
                .addonGroup(addonGroup)
                .mplanDetail(mplanDetail)
                .build();
    }

    public static Mplan from(UnapplyMplan unapplyMplan){
        return Mplan.builder()
                .name(unapplyMplan.getName())
                .addonGroup(unapplyMplan.getAddonGroup())
                .mplanDetail(unapplyMplan.getMplanDetail())
                .build();
    }
}
