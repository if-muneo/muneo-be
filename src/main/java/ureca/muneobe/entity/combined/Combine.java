package ureca.muneobe.entity.combined;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import ureca.muneobe.entity.combinedGroup.CombinedGroup;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Combine {

    @Id @GeneratedValue
    private Long id;

    private int combinationCount;

    private int discountPerPerson;

    private String type;

    private int combinedMaxCount;

    private int combinedMinCount;

    @OneToMany(mappedBy = "combine")
    private List<CombinedGroup> combinedGroups = new ArrayList<>();
}