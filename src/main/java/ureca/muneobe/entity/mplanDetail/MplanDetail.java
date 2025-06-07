package ureca.muneobe.entity.mplanDetail;

import jakarta.persistence.*;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.mplan.Mplan;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MplanDetail extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private int basicDataAmount;

    private String sharingData;

    private int monthlyPrice;

    private int voiceCallVolume;

    private String textMessage;

    private int dailyData;

    private int subDataSpeed;

    private DataType dataType;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Enumerated(EnumType.STRING)
    private MplanType mplanType;

    @OneToMany(mappedBy = "mplanDetail")
    private List<Mplan> mplan = new ArrayList<>();
}
