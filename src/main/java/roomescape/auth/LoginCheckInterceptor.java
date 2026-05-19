package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.UnauthorizedException;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final String LOGIN_MEMBER_ID = "loginMemberId";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LOGIN_MEMBER_ID) == null) {
            throw new UnauthorizedException();
        }
        return true;

    }

}
