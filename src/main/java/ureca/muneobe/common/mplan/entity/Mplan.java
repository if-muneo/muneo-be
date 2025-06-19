package ureca.muneobe.common.mplan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.review.entity.Review;
import ureca.muneobe.common.subscription.entity.Subscription;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;

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
