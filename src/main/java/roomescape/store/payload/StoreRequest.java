package roomescape.store.payload;

import jakarta.validation.constraints.NotNull;

public record StoreRequest(
        @NotNull(message = "매장 관리자 id는 필수입니다.")
        Long memberId
) {
}
