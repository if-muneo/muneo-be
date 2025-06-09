package ureca.muneobe.common.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ureca.muneobe.common.auth.entity.enums.Category;
import ureca.muneobe.common.auth.entity.enums.Gender;

@Getter
@AllArgsConstructor
public class SignupRequest {
    private String memberName;  // Member.name에 매핑
    private String password;
    private String phoneNumber;
    private String email;
    private Integer old;        // 나이 추가
    private Gender gender;      // 성별 추가 (필수라면)
    private Category category;  // 카테고리 추가 (필수라면)
}