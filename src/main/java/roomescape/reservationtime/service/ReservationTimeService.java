package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.NotFoundException;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.store.repository.StoreRepository;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final StoreRepository storeRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            StoreRepository storeRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public ReservationTime save(ReservationTimeRequest request) {
        ReservationTime reservationTime = ReservationTime.create(request.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAvailableReservationTimes(LocalDate date, Long themeId, Long storeId) {
        validateThemeExists(themeId);
        validateStoreExists(storeId);

        return reservationTimeRepository.findAvailableTimesByDateAndThemeIdAndStoreId(date, themeId, storeId);
    }

    @Transactional
    public void deleteById(Long id) {
        int affected = reservationTimeRepository.deleteById(id);
        if (affected == 0) {
            throw new NotFoundException(DomainType.RESERVATION_TIME, id);
        }
    }
    
    private void validateThemeExists(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new NotFoundException(DomainType.THEME, id);
        }
    }

    private void validateStoreExists(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new NotFoundException(DomainType.STORE, id);
        }
    }

}
