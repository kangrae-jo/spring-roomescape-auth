package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.payload.LoginRequest;
import roomescape.auth.payload.RegisterMemberRequest;
import roomescape.auth.service.AuthService;
import roomescape.member.entity.Member;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String LOGIN_MEMBER_ID = "loginMemberId";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterMemberRequest request
    ) {
        Member member = authService.register(request);

        URI location = URI.create("/members/" + member.getId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        Member member = authService.login(request);

        HttpSession session = servletRequest.getSession();
        servletRequest.changeSessionId();
        session.setAttribute(LOGIN_MEMBER_ID, member.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpSession session
    ) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

}
