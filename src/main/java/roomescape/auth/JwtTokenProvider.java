package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.UnauthorizedException;
import roomescape.member.entity.Role;

@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";

    private final Key secretKey;
    private final long validityInMilliseconds;
    private final Clock clock;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}")
            String rawSecretKey,
            @Value("${security.jwt.token.expire-length}")
            long validityInMilliseconds,
            Clock clock
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
        this.clock = clock;
    }

    public String createToken(Long memberId, Role role) {
        Date now = now();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim(ROLE_CLAIM, role.name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberId(String token) {
        try {
            Claims claims = getClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException();
        }
    }

    public Role getMemberRole(String token) {
        try {
            Claims claims = getClaims(token);
            return Role.valueOf(claims.get(ROLE_CLAIM, String.class));
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            throw new UnauthorizedException();
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setClock(this::now)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date now() {
        return Date.from(clock.instant());
    }

}
