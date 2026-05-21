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
import roomescape.store.service.StoreService;
import roomescape.theme.service.ThemeService;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeService themeService;
    private final StoreService storeService;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ThemeService themeService,
            StoreService storeService
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeService = themeService;
        this.storeService = storeService;
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
        themeService.validateExists(themeId);
        storeService.validateExists(storeId);

        return reservationTimeRepository.findAvailableTimesByDateAndThemeIdAndStoreId(date, themeId, storeId);
    }

    @Transactional
    public void deleteById(Long id) {
        int affected = reservationTimeRepository.deleteById(id);
        if (affected == 0) {
            throw new NotFoundException(DomainType.RESERVATION_TIME, id);
        }
    }

    @Transactional(readOnly = true)
    public ReservationTime getById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DomainType.RESERVATION_TIME, id));
    }

}
