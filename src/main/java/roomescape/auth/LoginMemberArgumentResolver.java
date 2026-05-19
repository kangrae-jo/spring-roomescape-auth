package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.exception.UnauthorizedException;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String LOGIN_MEMBER_ID = "loginMemberId";

    private final MemberRepository memberRepository;

    public LoginMemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && isMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedException();
        }

        Long memberId = (Long) session.getAttribute(LOGIN_MEMBER_ID);
        if (memberId == null) {
            throw new UnauthorizedException();
        }
        return memberRepository.findById(memberId)
                .orElseThrow(UnauthorizedException::new);
    }

}
