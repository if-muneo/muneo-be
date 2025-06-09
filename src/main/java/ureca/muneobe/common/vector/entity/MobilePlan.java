package ureca.muneobe.common.vector.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Slf4j
@Entity
@Table(name = "fat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean textMessages;
    private Long basicDataAmount;
    private Long dailyData;
    private Long monthlyPrice;
    private Long price;
    private Long sharingData;
    private Long subDataSpeed;
    private Long voiceCallVolume;
    private String addon;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Column(length = 255)
    private String description;

    @Column(name = "mplan_type")
    @Enumerated(EnumType.STRING)
    private MPlanType mplanType;

    private String name;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    private float[] embedding;

    // --- Enum 정의 ---
    public enum DataType {
        LTE_5G, NUGGET, SMART_DEVICE
    }

    public enum MPlanType {
        LTE, _5G
    }

    public enum Qualification {
        ALL, YOUTH, OLD, WELFARE, BOY, SOLDIER, KID
    }

    public String buildCombinedText() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append(qualification).append("(이)가 가입 할 수 있는 상품 입니다.").append("\n");

        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append("월 ").append(price / 10000).append("만원 대 요금제입니다.").append("\n");

        for(int i = (int) (price - 10000); i <= price + 10000; i+= 10000) {
            sb.append("\"").append(name).append("\"").append("이 요금제는 ");
            if(i < price) {
                sb.append(i / 10000).append("만원 이상 요금제입니다.").append("\n");
            } else {
                sb.append(i / 10000).append("만원 이하 요금제입니다.").append("\n");
            }
        }

        String serveData = (basicDataAmount == 10_000_000) ? "무제한" : (basicDataAmount / 1000) + "MB";
        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append(serveData).append("데이터를 제공합니다.").append("\n");

        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append(dataType).append("의 데이터 타입으로 구성 되어있습니다.").append("\n");

        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append(addon).append("을 제공합니다.").append("\n");

        sb.append("\"").append(name).append("\"").append("이 요금제는 ")
                .append("\"").append(description).append("\"").append("입니다.").append("\n");
        log.atInfo().log(sb.toString());
        return sb.toString();
    }
}
