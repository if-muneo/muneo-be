package ureca.muneobe.common.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.entity.enums.Role;

@Getter
@AllArgsConstructor
public class SessionMember {
    private Long id;
    private Role role;
    private String name;

    public static SessionMember from(Member member) {
        return new SessionMember(
                member.getId(),
                member.getRole(),
                member.getName()
        );
    }
}