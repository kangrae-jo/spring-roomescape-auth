package roomescape.common.exception;

public final class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super("인증되지 않은 사용자입니다.", "Unauthorized");
    }

}
