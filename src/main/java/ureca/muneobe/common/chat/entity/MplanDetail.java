package ureca.muneobe.common.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ureca.muneobe.common.common.BaseEntity;

@Entity
@Table(name = "mplan_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MplanDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "basic_data_amount")
    private int basicDataAmount;

    @Column(name = "daily_data")
    private int dailyData;

    @Column(name = "sharing_data")
    private int sharingData;

    @Column(name = "monthly_price")
    private int monthlyPrice;

    @Column(name = "voice_call_volume")
    private int voiceCallVolume;

    @Column(name = "text_message")
    private boolean textMessage;

    @Column(name = "sub_data_speed")
    private int subDataSpeed;

    @Column(name = "qualification")
    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "mplan_type")
    @Enumerated(EnumType.STRING)
    private MplanType mplanType;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @OneToMany(mappedBy = "mplanDetail")
    private List<Mplan> mplan = new ArrayList<>();
}
