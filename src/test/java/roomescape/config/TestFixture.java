package roomescape.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;

public final class TestFixture {

    private static final String TEST_JWT_SECRET_KEY = "/BWxvVt/eMsTVSq+RI9kRCrZKK38KNGIWi7ilxCg9So=";
    private static final long TEST_JWT_EXPIRE_LENGTH = 3600000L;
    private static final long LOGIN_MEMBER_ID = 1L;

    private TestFixture() {
    }

    public static ThemeRequest themeRequest(String name) {
        return new ThemeRequest(name, name + " 설명", "https://example.com/theme.png");
    }

    public static Theme theme(String name) {
        return Theme.create(name, name + " 설명", "https://example.com/theme.png", Theme.RUNTIME);
    }

    public static ReservationTimeRequest reservationTimeRequest(LocalTime startAt) {
        return new ReservationTimeRequest(startAt);
    }

    public static ReservationTime reservationTime(LocalTime startAt) {
        return ReservationTime.create(startAt);
    }

    public static ReservationRequest reservationRequest(
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        return new ReservationRequest(date, timeId, themeId);
    }

    public static ReservationUpdateRequest reservationupdateRequest(
            LocalDate date,
            Long timeId
    ) {
        return new ReservationUpdateRequest(date, timeId);
    }

    public static Reservation reservation(
            String name,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme
    ) {
        return Reservation.create(name, date, reservationTime, theme);
    }

    public static Map<String, Object> themeRequestBody(String name, String description, String thumbnailUrl) {
        return Map.of(
                "name", name,
                "description", description,
                "thumbnailUrl", thumbnailUrl
        );
    }

    public static Map<String, Object> reservationTimeRequestBody(String startAt) {
        return Map.of("startAt", startAt);
    }

    public static Map<String, Object> reservationRequestBody(LocalDate date, Long timeId, Long themeId) {
        return Map.of(
                "date", date.toString(),
                "timeId", timeId,
                "themeId", themeId
        );
    }

    public static Map<String, Object> reservationUpdateRequestBody(
            LocalDate date,
            Long timeId
    ) {
        return Map.of(
                "date", date.toString(),
                "timeId", timeId
        );
    }

    public static LocalDate futureReservationDate(Clock clock) {
        return LocalDate.now(clock).plusDays(2);
    }

    public static LocalDate nextReservationDate(Clock clock) {
        return futureReservationDate(clock).plusDays(1);
    }

    public static LocalDate pastReservationDate(Clock clock) {
        return LocalDate.now(clock).minusDays(1);
    }

    public static RequestPostProcessor loginMember() {
        return request -> {
            request.addHeader("Authorization", "Bearer " + createToken(LOGIN_MEMBER_ID));
            return request;
        };
    }

    private static String createToken(Long memberId) {
        byte[] keyBytes = Base64.getDecoder().decode(TEST_JWT_SECRET_KEY);
        Key secretKey = Keys.hmacShaKeyFor(keyBytes);
        Date now = new Date();
        Date validity = new Date(now.getTime() + TEST_JWT_EXPIRE_LENGTH);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

}
