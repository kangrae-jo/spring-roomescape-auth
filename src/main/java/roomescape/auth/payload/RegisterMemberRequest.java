package roomescape.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterMemberRequest(
        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 1, max = 10, message = "사용자 이름은 10자 이하입니다.")
        String name,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}
