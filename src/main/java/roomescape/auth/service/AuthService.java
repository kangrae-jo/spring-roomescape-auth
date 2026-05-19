package roomescape.auth.service;

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

    public AuthService(
            MemberRepository memberRepository,
            AuthCredentialRepository authCredentialRepository
    ) {
        this.memberRepository = memberRepository;
        this.authCredentialRepository = authCredentialRepository;
    }

    @Transactional
    public Member register(RegisterMemberRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new DuplicatedException(DomainType.MEMBER);
        }

        Member member = memberRepository.save(Member.create(request.name()));
        authCredentialRepository.save(AuthCredential.create(member.getId(), request.password()));

        return member;
    }

    @Transactional(readOnly = true)
    public Member login(LoginRequest request) {
        Member member = memberRepository.findByName(request.name())
                .orElseThrow(AuthenticationException::new);
        AuthCredential authCredential = authCredentialRepository.findByMemberId(member.getId())
                .orElseThrow(AuthenticationException::new);

        if (!authCredential.hasPassword(request.password())) {
            throw new AuthenticationException();
        }
        return member;
    }

}
