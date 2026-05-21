package roomescape.auth.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.payload.LoginRequest;
import roomescape.auth.payload.LoginResponse;
import roomescape.auth.payload.RegisterMemberRequest;
import roomescape.auth.service.AuthService;
import roomescape.member.entity.Member;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        Member member = authService.login(request);
        String accessToken = jwtTokenProvider.createToken(member.getId(), member.getRole());

        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

}
