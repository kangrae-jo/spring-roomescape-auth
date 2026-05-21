package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.UnauthorizedException;
import roomescape.member.entity.Role;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final String AUTHENTICATED_MEMBER_ID = "authenticatedMemberId";
    public static final String AUTHENTICATED_MEMBER_ROLE = "authenticatedMemberRole";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginCheckInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException();
        }

        String token = authorization.substring(BEARER_PREFIX.length()).strip();
        if (token.isBlank()) {
            throw new UnauthorizedException();
        }

        Long memberId = jwtTokenProvider.getMemberId(token);
        request.setAttribute(AUTHENTICATED_MEMBER_ID, memberId);
        
        Role role = jwtTokenProvider.getMemberRole(token);
        request.setAttribute(AUTHENTICATED_MEMBER_ROLE, role);

        return true;
    }

}
