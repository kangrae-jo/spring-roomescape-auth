package roomescape.member.entity;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String name;

    private Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Member create(String name) {
        return new Member(null, name);
    }

    public static Member of(Long id, String name) {
        return new Member(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
