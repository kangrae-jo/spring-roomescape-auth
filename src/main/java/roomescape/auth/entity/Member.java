package roomescape.auth.entity;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String name;
    private final String password;

    private Member(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static Member create(String name, String password) {
        return new Member(null, name, password);
    }

    public static Member of(Long id, String name, String password) {
        return new Member(id, name, password);
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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
