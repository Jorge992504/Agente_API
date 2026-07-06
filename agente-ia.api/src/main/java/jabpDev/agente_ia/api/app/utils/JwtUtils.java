package jabpDev.agente_ia.api.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jabpDev.agente_ia.api.app.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 7L * 24 * 60 * 1000;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(String email) {
        return Jwts.builder()
                .subject(String.valueOf(email))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    public String getUsuarioByToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject(); // agora é o e-mail, não mais um Long
    }
}
