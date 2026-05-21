package roomescape.member.entity;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String name;
    private final Role role;

    private Member(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static Member create(String name) {
        return new Member(null, name, Role.GUEST);
    }

    public static Member of(Long id, String name) {
        return new Member(id, name, Role.GUEST);
    }

    public static Member of(Long id, String name, Role role) {
        return new Member(id, name, role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
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
