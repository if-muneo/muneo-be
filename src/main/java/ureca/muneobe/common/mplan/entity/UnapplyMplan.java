package ureca.muneobe.common.mplan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;

@Entity
@Table(name = "unapply_mplan")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnapplyMplan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mplan_seq_generator")
    @SequenceGenerator(
            name = "mplan_seq_generator",
            sequenceName = "mplan_seq",
            initialValue = 1,
            allocationSize = 100
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mplan_detail_id")
    private MplanDetail mplanDetail;

    public static UnapplyMplan of(MplanCreateRequest mplanCreateRequest, AddonGroup addonGroup,
                                  MplanDetail mplanDetail) {
            return UnapplyMplan.builder()
                    .name(mplanCreateRequest.getName())
                    .addonGroup(addonGroup)
                    .mplanDetail(mplanDetail)
                    .build();
    }
}
