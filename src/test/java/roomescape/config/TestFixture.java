package roomescape.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import roomescape.auth.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.store.entity.Store;
import roomescape.store.payload.StoreRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;

public final class TestFixture {

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
        return reservationRequest(date, timeId, themeId, 1L);
    }

    public static ReservationRequest reservationRequest(
            LocalDate date,
            Long timeId,
            Long themeId,
            Long storeId
    ) {
        return new ReservationRequest(date, timeId, themeId, storeId);
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
        return reservation(name, date, reservationTime, theme, Store.of(1L, Member.of(1L, "manager")));
    }

    public static Reservation reservation(
            String name,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme,
            Store store
    ) {
        return Reservation.create(name, date, reservationTime, theme, store);
    }

    public static Member member(String name) {
        return Member.create(name);
    }

    public static Store store(Member manager) {
        return Store.create(manager);
    }

    public static StoreRequest storeRequest(Long memberId) {
        return new StoreRequest(memberId);
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
        return reservationRequestBody(date, timeId, themeId, 1L);
    }

    public static Map<String, Object> reservationRequestBody(
            LocalDate date,
            Long timeId,
            Long themeId,
            Long storeId
    ) {
        return Map.of(
                "date", date.toString(),
                "timeId", timeId,
                "themeId", themeId,
                "storeId", storeId
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

    public static RequestPostProcessor loginMember(JwtTokenProvider jwtTokenProvider) {
        return loginMember(jwtTokenProvider, LOGIN_MEMBER_ID, Role.ADMIN);
    }

    public static RequestPostProcessor loginMember(JwtTokenProvider jwtTokenProvider, Long memberId, Role role) {
        return request -> {
            String accessToken = jwtTokenProvider.createToken(memberId, role);
            request.addHeader("Authorization", "Bearer " + accessToken);
            return request;
        };
    }

}
