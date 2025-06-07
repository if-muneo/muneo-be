package ureca.muneobe.entity.addon;

import jakarta.persistence.*;

@Entity
public class Addon {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private int price;

    @Enumerated(EnumType.STRING)
    private AddonType addonType;
}
