package roomescape.reservation.entity;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.store.entity.Store;
import roomescape.theme.entity.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Store store;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme, Store store) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.store = store;
    }

    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme, Store store) {
        return new Reservation(null, name, date, time, theme, store);
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time, Theme theme, Store store) {
        return new Reservation(id, name, date, time, theme, store);
    }

    public boolean isOwner(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Store getStore() {
        return store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
