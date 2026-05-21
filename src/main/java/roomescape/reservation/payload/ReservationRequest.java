package roomescape.reservation.payload;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,
        @NotNull(message = "예약 시간id는 필수입니다.")
        Long timeId,
        @NotNull(message = "테마 id는 필수입니다.")
        Long themeId,
        @NotNull(message = "매장 id는 필수입니다.")
        Long storeId
) {
}
