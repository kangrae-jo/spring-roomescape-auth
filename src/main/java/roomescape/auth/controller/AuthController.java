package roomescape.auth.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.entity.Member;
import roomescape.auth.payload.LoginRequest;
import roomescape.auth.payload.RegisterMemberRequest;
import roomescape.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
            HttpSession session
    ) {
        Member member = authService.login(request);
        session.setAttribute("loginMember", member);

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
