package ureca.muneobe.common.chat.entity;

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
    List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "mplan")
    List<Subscription> subscriptions = new ArrayList<>();

    public static Mplan of(MplanCreateRequest mplanCreateRequest, AddonGroup addonGroup, MplanDetail mplanDetail){
        return Mplan.builder()
                .name(mplanCreateRequest.getName())
                .addonGroup(addonGroup)
                .mplanDetail(mplanDetail)
                .build();
    }
}