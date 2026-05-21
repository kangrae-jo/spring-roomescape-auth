package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.AccessDeniedException;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.UnauthorizedException;
import roomescape.member.entity.Role;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Long memberId = (Long) request.getAttribute(LoginCheckInterceptor.AUTHENTICATED_MEMBER_ID);
        Role role = (Role) request.getAttribute(LoginCheckInterceptor.AUTHENTICATED_MEMBER_ROLE);
        if (memberId == null || role == null) {
            throw new UnauthorizedException();
        }

        if (role != Role.ADMIN) {
            throw new AccessDeniedException(DomainType.MEMBER, memberId);
        }

        return true;
    }

}
