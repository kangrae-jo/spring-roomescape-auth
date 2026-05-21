package roomescape.store.entity;

import java.util.Objects;
import roomescape.member.entity.Member;

public class Store {

    private final Long id;
    private final Member manager;

    private Store(Long id, Member manager) {
        this.id = id;
        this.manager = manager;
    }

    public static Store create(Member manager) {
        return new Store(null, manager);
    }

    public static Store of(Long id, Member manager) {
        return new Store(id, manager);
    }

    public Long getId() {
        return id;
    }

    public Member getManager() {
        return manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Store store = (Store) o;
        return id != null && Objects.equals(id, store.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
