package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.DuplicatedException;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.store.entity.Store;
import roomescape.theme.entity.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_WITH_TIME_AND_THEME = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date,
                rt.id AS time_id,
                rt.start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description,
                t.thumbnail_url,
                t.runtime,
                s.id AS store_id,
                m.id AS manager_id,
                m.name AS manager_name,
                m.role AS manager_role
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            INNER JOIN store s
                ON r.store_id = s.id
            INNER JOIN member m
                ON s.member_id = m.id
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
            Reservation.of(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getObject("date", LocalDate.class),
                    ReservationTime.of(
                            rs.getLong("time_id"),
                            rs.getObject("start_at", LocalTime.class)
                    ),
                    Theme.of(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail_url"),
                            Duration.ofMinutes(rs.getLong("runtime"))
                    ),
                    Store.of(
                            rs.getLong("store_id"),
                            Member.of(
                                    rs.getLong("manager_id"),
                                    rs.getString("manager_name"),
                                    Role.valueOf(rs.getString("manager_role"))
                            )
                    )
            );

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id, store_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, reservation.getName());
                ps.setObject(2, reservation.getDate());
                ps.setLong(3, reservation.getTime().getId());
                ps.setLong(4, reservation.getTheme().getId());
                ps.setLong(5, reservation.getStore().getId());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DuplicatedException(
                    DomainType.RESERVATION,
                    reservation.getDate(),
                    reservation.getTime().getId(),
                    reservation.getTheme().getId(),
                    reservation.getStore().getId()
            );
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Reservation.of(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme(),
                reservation.getStore()
        );
    }

    @Override
    public Reservation update(Reservation reservation, LocalDate date, ReservationTime reservationTime) {
        String sql = """
                UPDATE reservation
                SET date = ?, time_id = ?
                WHERE id = ?
                """;

        try {
            jdbcTemplate.update(
                    sql,
                    date,
                    reservationTime.getId(),
                    reservation.getId()
            );
        } catch (DuplicateKeyException e) {
            throw new DuplicatedException(
                    DomainType.RESERVATION,
                    date,
                    reservationTime.getId(),
                    reservation.getTheme().getId(),
                    reservation.getStore().getId()
            );
        }

        return Reservation.of(
                reservation.getId(),
                reservation.getName(),
                date,
                reservationTime,
                reservation.getTheme(),
                reservation.getStore()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_RESERVATION_WITH_TIME_AND_THEME + "WHERE r.id = ?";

        List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);

        return result.stream().findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = SELECT_RESERVATION_WITH_TIME_AND_THEME + "ORDER BY r.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public List<Reservation> findAllByName(String name) {
        String sql = SELECT_RESERVATION_WITH_TIME_AND_THEME + "WHERE r.name = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, name);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
