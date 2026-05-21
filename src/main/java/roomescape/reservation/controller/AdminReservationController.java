package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.member.entity.Member;
import roomescape.reservation.payload.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations(
            @LoginMember Member member
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findAll(member).stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @LoginMember Member member,
            @PathVariable Long id
    ) {
        reservationService.deleteById(id, member);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
