package ureca.muneobe.common.vector.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fat")
@Getter
@Setter
@ToString
public class Fat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text_messages")
    private Boolean textMessages;

    @Column(name = "basic_data_amount")
    private Long basicDataAmount;

    @Column(name = "daily_data")
    private Long dailyData;

    @Column(name = "monthly_price")
    private Long monthlyPrice;

    @Column(name = "price")
    private Long price;

    @Column(name = "sharing_data")
    private Long sharingData;

    @Column(name = "sub_data_speed")
    private Long subDataSpeed;

    @Column(name = "voice_call_volume")
    private Long voiceCallVolume;

    @Column(name = "addon")
    private String addon;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "mplan_type")
    private String mplanType;

    @Column(name = "name")
    private String name;

    @Column(name = "qualification")
    private String qualification;

    // 현재 이 요금제가 임베딩 값 테이블에 들어가있는지 안들어가있는지 확인하는 변수
    @Column(name = "is_embedding", nullable = false)
    private Boolean embedding = false;

    public List<String> makeDisriptionForEmbedding() {
        List<String> sentences = new ArrayList<>();
        sentences.add(makeSentenceToPrice(new StringBuilder()));
        sentences.add(makeSentencesToQualification(new StringBuilder()));
        sentences.addAll(makeSentencesToDescription());
        sentences.add(makeSentenceToAddon(new StringBuilder()));
        sentences.add(makeSentenceToBasicDataAmount(new StringBuilder()));
        sentences.add(makeSentencesToDataType(new StringBuilder()));
        return sentences;
    }

    /**
     * OO요금제는 OO만원 대 요금제입니다.
     * OO요금제는 OO만원 이상 요금제입니다.
     */
    private String makeSentenceToPrice(StringBuilder sentence) {
        sentence.append(name).append("요금제는 ");
        Long simplePrice = price / 10_000;
        sentence.append(simplePrice).append("만원 대 요금제입니다.").append("\n");

        sentence.append(name).append("요금제는 ");
        sentence.append(simplePrice).append("만원 이상 요금제입니다.").append("\n");
        return sentence.toString();
    }

    /**
     * OO요금제는 OO 데이터양을 제공합니다.
     */
    private String makeSentenceToBasicDataAmount(StringBuilder sentence) {
        sentence.append(name).append("요금제는 ");

        Long simpleBasicDataAmount = basicDataAmount / 1000;
        String serveDataAmount = "무제한";

        if(simpleBasicDataAmount < 10_000) {
            serveDataAmount = simpleBasicDataAmount.toString();
            serveDataAmount += "MB";
        }
        sentence.append(serveDataAmount).append(" 데이터양을 제공합니다.");
        return sentence.toString();
    }

    /**
     * OO요금제는 부가서비스 OO을 제공합니다.
     */
    private String makeSentenceToAddon(StringBuilder sentence) {
        sentence.append(name).append("요금제는 ");
        sentence.append("부가서비스 ").append(addon).append("을(를) 제공합니다.");
        return sentence.toString();
    }

    /**
     * OO요금제에 대한 description 설명
     */
    private List<String> makeSentencesToDescription() {
        return new ArrayList<>(List.of(description.split("\\$")));
    }

    /**
     * OO을 위한 요금제
     */
    private String makeSentencesToQualification(StringBuilder sentence) {
        String newQulification = qualification;
        switch (newQulification) {
            case "ALL" : newQulification = "모든 사람";
            break;
            case "YOUTH" : newQulification = "청년";
            break;
            case "OLD" : newQulification ="노인";
            break;
            case "WELFARE" : newQulification ="복지";
            break;
            case "BOY" : newQulification = "청소년";
            break;
            case "SOLDIER" : newQulification = "군인";
            break;
            case "KID" : newQulification = "아이";
            break;
        }

        sentence.append(newQulification).append("을(를) 위한 요금제");
        return sentence.toString();
    }

    /**
     *  OO 요금제는 OO 데이터 타입을 제공합니다.
     */
    private String makeSentencesToDataType(StringBuilder sentence) {
        sentence.append(name).append("요금제는 ");
        sentence.append(dataType).append("데이터 타입을 제공합니다.");
        return sentence.toString();
    }


    /**
     * 2차 llm으로 전달하기 위한 메서드
     */
    public String makeDiscriptionForResponse() {
        StringBuilder summary = new StringBuilder();

        summary.append("요금제명: ").append(name).append(", "); //요금제명

        summary.append("월 요금: ").append(price).append("원, "); //한달 요금

        if (basicDataAmount == null) {
            summary.append("일일 데이터: ").append(dailyData).append("MB, "); //없으면 일일데이터량
        } else {
            String state = (basicDataAmount == 10_000_000) ? "무제한, " : basicDataAmount + "MB, ";
            summary.append("월 데이터: ").append(state);
        }

        if (mplanType != null && !mplanType.isEmpty()) {
            summary.append("타입: ").append(mplanType).append(", "); //데이터 타입
        }

        if (addon != null && !addon.isEmpty()) {
            summary.append("부가서비스: ").append(addon).append(", "); //부가서비스 제공된 것
        }

        //마지막 구분자 제거
        summary.setLength(summary.length() - 2);

        return summary.toString();
    }
}
