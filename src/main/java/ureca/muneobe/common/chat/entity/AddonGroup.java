package ureca.muneobe.common.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addon_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddonGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "addon_group_name")
    private String addonGroupName;

    @OneToMany(mappedBy = "addonGroup")
    private List<Addon> addon = new ArrayList<>();
}
