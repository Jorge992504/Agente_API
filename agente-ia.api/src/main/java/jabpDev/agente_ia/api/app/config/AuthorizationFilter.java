package jabpDev.agente_ia.api.app.config;


import jabpDev.agente_ia.api.app.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    // Rotas que não exigem token (regra de negócio combinada)
    private static final List<String> ROTAS_PUBLICAS = List.of("/auth/login", "/auth/register", "/pagamento/webhook");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean isWebSocketHandshake = path.contains("/chat/");
        boolean publica = ROTAS_PUBLICAS.stream().anyMatch(path::endsWith) || isWebSocketHandshake;

        if (!publica) {
            String header = request.getHeader("Authorization");
            String token = (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;

            if (token == null || !jwtUtils.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Token inválido ou expirado\",\"code\":401}");
                return;
            }

            String email = jwtUtils.getUsuarioByToken(token);
            var auth = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
