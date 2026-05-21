package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAll();

    List<ReservationTime> findAvailableTimesByDateAndThemeIdAndStoreId(LocalDate date, Long themeId, Long storeId);

    int deleteById(Long id);

}
