package roomescape.reservation.payload;

import java.time.LocalDate;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.store.payload.StoreResponse;
import roomescape.theme.payload.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        StoreResponse store
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                StoreResponse.from(reservation.getStore())
        );
    }

}
