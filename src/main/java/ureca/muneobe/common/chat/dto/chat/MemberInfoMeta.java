package ureca.muneobe.common.chat.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoMeta {

    private String memberName;
    private Integer useAmount;
    private Integer fee;
    private String name;
    private String basicDataAmount;
    private String dataType;  // String으로 받음
    private String addonNamesStr;
    private String mplanName;
    private String mplanDetailStr;
    private String addonGroupStr;

    public MemberInfoMeta(String memberName, Integer useAmount, Integer fee,
                          String name, String basicDataAmount, Object dataType,  // Object로 받음
                          String addonNamesStr, String mplanName,
                          String mplanDetailStr, String addonGroupStr) {
        this.memberName = memberName;
        this.useAmount = useAmount;
        this.fee = fee;
        this.name = name;
        this.basicDataAmount = basicDataAmount;
        this.dataType = dataType != null ? dataType.toString() : "UNKNOWN";  // ✅ toString() 활용
        this.addonNamesStr = addonNamesStr;
        this.mplanName = mplanName;
        this.mplanDetailStr = mplanDetailStr;
        this.addonGroupStr = addonGroupStr;
    }

}