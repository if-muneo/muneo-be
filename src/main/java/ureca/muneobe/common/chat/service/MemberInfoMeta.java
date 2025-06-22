package ureca.muneobe.common.chat.service;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.mplan.entity.DataType;

@Getter
@NoArgsConstructor
public class MemberInfoMeta {
    //member 정보
    private String memberName;
    private Integer useAmount;

    //subscription 정보
    private Integer fee;

    //가입된 모바일 요금제 정보
    private String name;
    private Integer basicDataAmount;
    private DataType dataType;

    //mplan에 포함된 부가서비스 이름들
    private List<String> addonNames;
}
