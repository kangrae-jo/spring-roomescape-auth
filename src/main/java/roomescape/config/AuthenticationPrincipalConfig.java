package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginCheckInterceptor;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.member.repository.MemberRepository;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private static final String[] STATIC_RESOURCES = {
            "/",
            "/index.html",
            "/app.js",
            "/styles.css",
            "/favicon.ico",
            "/error"
    };

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final MemberRepository memberRepository;

    public AuthenticationPrincipalConfig(
            LoginCheckInterceptor loginCheckInterceptor,
            MemberRepository memberRepository
    ) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCES)
                .excludePathPatterns(
                        "/auth/register",
                        "/auth/login",
                        "/stores",
                        "/times/**",
                        "/themes/**"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberRepository));
    }

}
