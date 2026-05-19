package roomescape.member.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.entity.Member;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            Member.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    @Override
    public Member save(Member member) {
        String sql = """
                INSERT INTO member (name)
                VALUES (?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getName());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Member.of(id, member.getName());
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "SELECT id, name FROM member WHERE name = ?";
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper, name);
        return result.stream().findFirst();
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 FROM member WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, name));
    }

}
