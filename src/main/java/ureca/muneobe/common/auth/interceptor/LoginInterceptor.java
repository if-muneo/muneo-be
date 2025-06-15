package ureca.muneobe.common.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.auth.utils.SessionUtil;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final String[] PUBLIC_PATTERNS = {
            "/", "/login", "/signup", "/api/auth"
    };
    //TODO: /api 경로 수정 예정
    private static final String[] LOGIN_REQUIRED = {
            "/api", "/admin"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (isPublicPatterns(requestURI)) {
            return true;
        }

        if (requireLogin(requestURI)) {
            if (!SessionUtil.isLoggedIn(session)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"로그인이 필요합니다\"}");
                return false;
            }

            // 관리자 권한이 필요한 URL 체크
            if (requestURI.startsWith("/admin")) {
                if (!SessionUtil.hasRole(session, Role.ADMIN)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 필요합니다");
                    return false;
                }
            }
        }

        return true;
    }

    //TODO : 프론트와 약속된 비동기 요청 처리중 (이외의 비동기 요청은 고려 or 추가)
    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

//        String accept = request.getHeader("Accept");
//        return accept != null && accept.contains("application/json");
//        String contentType = request.getHeader("Content-Type");
//        return contentType != null && contentType.contains("application/json");
    }

    private boolean requireLogin(String requestURI) {
        for (String pattern : LOGIN_REQUIRED) {
            if (requestURI.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPublicPatterns(String requestURI) {
        for (String pattern : PUBLIC_PATTERNS) {
            if (requestURI.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

}
