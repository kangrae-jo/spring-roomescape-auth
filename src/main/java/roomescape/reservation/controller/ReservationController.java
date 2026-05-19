package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.member.entity.Member;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationResponse;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(
            @Valid @RequestBody ReservationRequest request
    ) {
        Reservation reservation = reservationService.save(request);
        URI location = URI.create("/reservations/" + reservation.getId());

        return ResponseEntity.created(location)
                .body(ReservationResponse.from(reservation));
    }

    @PatchMapping("/{id}/schedule")
    public ResponseEntity<ReservationResponse> updateReservationSchedule(
            @LoginMember Member member,
            @PathVariable Long id,
            @Valid @RequestBody ReservationUpdateRequest request
    ) {
        Reservation reservation = reservationService.update(id, member.getName(), request);
        URI location = URI.create("/reservations/" + reservation.getId());

        return ResponseEntity.ok().location(location)
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @LoginMember Member member
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findAllByName(member.getName()).stream()
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
        reservationService.deleteByIdAndName(id, member.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
