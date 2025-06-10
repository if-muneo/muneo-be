package ureca.muneobe.common.auth.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.auth.dto.SessionMember;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.entity.enums.Role;

@Component
public class SessionUtil {

    public static final String MEMBER_SESSION_KEY = "loginMember";

    public static void setMemberSession(HttpSession session, Member member) {
        session.setAttribute(MEMBER_SESSION_KEY, SessionMember.from(member));
//        session.setMaxInactiveInterval(30*60); yml에서 해당 설정 완료
    }
    // 세션에서 로그인 사용자 정보 가져오기
    public static SessionMember getLoginMember(HttpSession session) {
        if (session == null) return null;
        return (SessionMember) session.getAttribute(MEMBER_SESSION_KEY);
    }

    // 로그인 여부 확인
    public static boolean isLoggedIn(HttpSession session) {
        return getLoginMember(session) != null;
    }

    // 특정 권한 확인
    public static boolean hasRole(HttpSession session, Role role) {
        SessionMember member = getLoginMember(session);
        return member != null && member.getRole() == role;
    }

    // 로그아웃
    public static void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
