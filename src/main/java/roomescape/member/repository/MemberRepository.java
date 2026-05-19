package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.entity.Member;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByName(String name);

    boolean existsByName(String name);

}
