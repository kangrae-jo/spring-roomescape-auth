package roomescape.auth.repository;

import java.util.Optional;
import roomescape.auth.entity.AuthCredential;

public interface AuthCredentialRepository {

    AuthCredential save(AuthCredential authCredential);

    Optional<AuthCredential> findByMemberId(Long memberId);

}
