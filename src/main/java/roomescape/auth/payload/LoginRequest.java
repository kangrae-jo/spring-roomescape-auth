package roomescape.auth.payload;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String name,
        @NotBlank
        String password
) {
}
