package roomescape.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.entity.AuthCredential;
import roomescape.auth.payload.LoginRequest;
import roomescape.auth.payload.RegisterMemberRequest;
import roomescape.auth.repository.AuthCredentialRepository;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.DuplicatedException;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthCredentialRepository authCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            MemberRepository memberRepository,
            AuthCredentialRepository authCredentialRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.authCredentialRepository = authCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member register(RegisterMemberRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new DuplicatedException(DomainType.MEMBER);
        }

        Member member = memberRepository.save(Member.create(request.name()));
        String passwordHash = passwordEncoder.encode(request.password());
        authCredentialRepository.save(AuthCredential.createWithPasswordHash(member.getId(), passwordHash));

        return member;
    }

    @Transactional(readOnly = true)
    public Member login(LoginRequest request) {
        Member member = memberRepository.findByName(request.name())
                .orElseThrow(AuthenticationException::new);
        AuthCredential authCredential = authCredentialRepository.findByMemberId(member.getId())
                .orElseThrow(AuthenticationException::new);
        
        if (!passwordEncoder.matches(request.password(), authCredential.getPasswordHash())) {
            throw new AuthenticationException();

        }
        return member;
    }

}
