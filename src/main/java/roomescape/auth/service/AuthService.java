package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.Member;
import roomescape.auth.payload.LoginRequest;
import roomescape.auth.payload.RegisterMemberRequest;
import roomescape.auth.repository.AuthRepository;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.DuplicatedException;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Member register(RegisterMemberRequest request) {
        if (authRepository.existsByName(request.name())) {
            throw new DuplicatedException(DomainType.MEMBER);
        }

        Member member = Member.create(request.name(), request.password());
        return authRepository.save(member);
    }

    public Member login(LoginRequest request) {
        Member member = authRepository.findByName(request.name())
                .orElseThrow(AuthenticationException::new);

        if (!member.hasPassword(request.password())) {
            throw new AuthenticationException();
        }

        return member;
    }

}
