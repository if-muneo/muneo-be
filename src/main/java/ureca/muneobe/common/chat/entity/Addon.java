package ureca.muneobe.common.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Addon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "addon_type")
    @Enumerated(EnumType.STRING)
    private AddonType addonType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addon_group_id")
    private AddonGroup addonGroup;
}
