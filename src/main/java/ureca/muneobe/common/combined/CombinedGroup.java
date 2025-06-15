package ureca.muneobe.common.combined;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.subscription.entity.Subscription;

@Entity
@Table(name = "combined")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinedGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combined_id")
    private Combined combined;

    @OneToMany(mappedBy = "combinedGroup")
    private List<Subscription> subscriptions = new ArrayList<>();
}
