package roomescape.auth.entity;

import java.util.Objects;

public class AuthCredential {

    private final Long id;
    private final Long memberId;
    private final String password;

    private AuthCredential(Long id, Long memberId, String password) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
    }

    public static AuthCredential create(Long memberId, String password) {
        return new AuthCredential(null, memberId, password);
    }

    public static AuthCredential of(Long id, Long memberId, String password) {
        return new AuthCredential(id, memberId, password);
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
        AuthCredential that = (AuthCredential) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
