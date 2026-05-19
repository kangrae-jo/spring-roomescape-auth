package roomescape.auth.entity;

import java.util.Objects;

public class AuthCredential {

    private final Long id;
    private final Long memberId;
    private final String passwordHash;

    private AuthCredential(Long id, Long memberId, String passwordHash) {
        this.id = id;
        this.memberId = memberId;
        this.passwordHash = passwordHash;
    }

    public static AuthCredential createWithPasswordHash(Long memberId, String passwordHash) {
        return new AuthCredential(null, memberId, passwordHash);
    }

    public static AuthCredential of(Long id, Long memberId, String passwordHash) {
        return new AuthCredential(id, memberId, passwordHash);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getPasswordHash() {
        return passwordHash;
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
