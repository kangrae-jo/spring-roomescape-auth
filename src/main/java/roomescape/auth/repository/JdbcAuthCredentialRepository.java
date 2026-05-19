package roomescape.auth.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.auth.entity.AuthCredential;

@Repository
public class JdbcAuthCredentialRepository implements AuthCredentialRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthCredentialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AuthCredential> authCredentialRowMapper = (rs, rowNum) ->
            AuthCredential.of(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getString("password_hash")
            );

    @Override
    public AuthCredential save(AuthCredential authCredential) {
        String sql = """
                INSERT INTO auth_credential (member_id, password_hash)
                VALUES (?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, authCredential.getMemberId());
            ps.setString(2, authCredential.getPasswordHash());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return AuthCredential.of(id, authCredential.getMemberId(), authCredential.getPasswordHash());
    }

    @Override
    public Optional<AuthCredential> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, password_hash FROM auth_credential WHERE member_id = ?";
        List<AuthCredential> result = jdbcTemplate.query(sql, authCredentialRowMapper, memberId);
        return result.stream().findFirst();
    }

}
