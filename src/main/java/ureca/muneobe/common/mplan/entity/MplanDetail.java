package ureca.muneobe.common.mplan.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;
import ureca.muneobe.common.mplan.dto.request.MplanDetailCreateRequest;
import ureca.muneobe.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mplan_detail")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MplanDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "basic_data_amount")
    private Integer basicDataAmount;

    @Column(name = "daily_data")
    private Integer dailyData;

    @Column(name = "sharing_data")
    private Integer sharingData;

    @Column(name = "monthly_price")
    private Integer monthlyPrice;

    @Column(name = "voice_call_volume")
    private Integer voiceCallVolume;

    @Column(name = "text_message")
    private Boolean textMessage;

    @Column(name = "sub_data_speed")
    private Integer subDataSpeed;

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
    @Builder.Default
    private List<Mplan> mplan = new ArrayList<>();

    public static MplanDetail from(MplanDetailCreateRequest request){
        return MplanDetail.builder()
                .basicDataAmount(request.getBasicDataAmount())
                .dailyData(request.getDailyData())
                .sharingData(request.getSharingData())
                .monthlyPrice(request.getMonthlyPrice())
                .voiceCallVolume(request.getVoiceCallVolume())
                .textMessage(request.getTextMessage())
                .subDataSpeed(request.getSubDataSpeed())
                .qualification(request.getQualification())
                .mplanType(request.getMplanType())
                .dataType(request.getDataType())
                .build();
    }

    public static MplanDetail from(MplanCreateRequest mplanDetailCreateRequest){
        return MplanDetail.builder()
                .basicDataAmount(mplanDetailCreateRequest.getBasicDataAmount())
                .dailyData(mplanDetailCreateRequest.getDailyData())
                .sharingData(mplanDetailCreateRequest.getSharingData())
                .monthlyPrice(mplanDetailCreateRequest.getMonthlyPrice())
                .voiceCallVolume(mplanDetailCreateRequest.getVoiceCallVolume())
                .textMessage(mplanDetailCreateRequest.getTextMessage())
                .subDataSpeed(mplanDetailCreateRequest.getSubDataSpeed())
                .qualification(mplanDetailCreateRequest.getQualification())
                .mplanType(mplanDetailCreateRequest.getMplanType())
                .dataType(mplanDetailCreateRequest.getDataType())
                .build();
    }
}
