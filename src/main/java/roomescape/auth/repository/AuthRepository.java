package roomescape.auth.repository;

import java.util.Optional;
import roomescape.auth.entity.Member;

public interface AuthRepository {

    Member save(Member member);

    Optional<Member> findByName(String name);

    boolean existsByName(String name);

}
