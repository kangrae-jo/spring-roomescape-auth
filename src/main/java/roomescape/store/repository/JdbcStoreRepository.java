package roomescape.store.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.InUseException;
import roomescape.member.entity.Member;
import roomescape.store.entity.Store;

@Repository
public class JdbcStoreRepository implements StoreRepository {

    private static final String SELECT_STORE_WITH_MANAGER = """
            SELECT
                s.id AS store_id,
                m.id AS member_id,
                m.name AS member_name
            FROM store s
            INNER JOIN member m
                ON s.member_id = m.id
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Store> storeRowMapper = (rs, rowNum) ->
            Store.of(
                    rs.getLong("store_id"),
                    Member.of(
                            rs.getLong("member_id"),
                            rs.getString("member_name")
                    )
            );

    public JdbcStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM store WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public Store save(Store store) {
        String sql = "INSERT INTO store (member_id) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, store.getManager().getId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Store.of(id, store.getManager());
    }

    @Override
    public Optional<Store> findById(Long id) {
        String sql = SELECT_STORE_WITH_MANAGER + "WHERE s.id = ?";
        List<Store> result = jdbcTemplate.query(sql, storeRowMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public List<Store> findAll() {
        String sql = SELECT_STORE_WITH_MANAGER + "ORDER BY s.id";
        return jdbcTemplate.query(sql, storeRowMapper);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM store WHERE id = ?";

        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new InUseException(DomainType.STORE, id, e);
        }
    }

}
