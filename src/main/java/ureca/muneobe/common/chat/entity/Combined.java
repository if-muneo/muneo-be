package ureca.muneobe.common.chat.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "combined")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Combined {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private Integer count;

    @Column(name = "discount_per_person")
    private Integer discountPerPerson;

    @Column(name = "combined_type")
    @Enumerated(EnumType.STRING)
    private CombinedType combinedType;

    @OneToMany(mappedBy = "combined")
    private List<CombinedGroup> combinedGroups = new ArrayList<>();
}
