package ureca.muneobe.common.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.entity.enums.Category;
import ureca.muneobe.common.auth.entity.enums.Gender;
import ureca.muneobe.common.auth.entity.enums.Role;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private Integer old;
    private Gender gender;
    private String name;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean activeYn;
    private Role role;
    private Integer useAmount;
    private SubscriptionsResponse subscriptionsResponse;

    public static MyPageResponse from(Member member) {
        return MyPageResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .old(member.getOld())
                .gender(member.getGender())
                .name(member.getName())
                .category(member.getCategory())
                .createdAt(member.getCreatedAt())
                .deletedAt(member.getDeletedAt())
                .activeYn(member.getActiveYn())
                .role(member.getRole())
                .useAmount(member.getUseAmount())
                .subscriptionsResponse(SubscriptionsResponse.from(member.getSubscriptions()))
                .build();
    }
}
