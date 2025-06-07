package ureca.muneobe.entity.fat;

import jakarta.persistence.*;
import ureca.muneobe.entity.mplanDetail.DataType;
import ureca.muneobe.entity.mplanDetail.MplanType;
import ureca.muneobe.entity.mplanDetail.Qualification;

@Entity
public class Fat {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long price;

    @Column(name = "basic_data_amount")
    private Long basicDataAmount;

    @Column(name = "daily_data")
    private Long dailyData;

    @Column(name = "sharing_data")
    private Long sharingData;

    @Column(name = "monthly_price")
    private Long monthlyPrice;

    @Column(name = "voice_call_volume")
    private Long voiceCallVolume;

    @Column(name = "text_messages")
    private Boolean textMessages;

    @Column(name = "sub_data_speed")
    private Long subDataSpeed;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "mplan_type")
    @Enumerated(EnumType.STRING)
    private MplanType mplanType;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    private String addon;

    private String description;
}
