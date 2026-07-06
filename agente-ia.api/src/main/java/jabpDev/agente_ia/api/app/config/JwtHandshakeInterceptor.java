package jabpDev.agente_ia.api.app.config;

import jabpDev.agente_ia.api.app.entity.Usuario;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.app.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private JwtUtils jwtUtils;
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        String token = extrairToken(query);

        if (token == null || !jwtUtils.isTokenValid(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        // Antes: jwtUtils.getIdUsuario(token) tentava Long.valueOf(email) e quebrava aqui
        String email = jwtUtils.getEmail(token);
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        attributes.put("email", usuario.get().getEmail());

        String path = request.getURI().getPath();
        String idConversaStr = path.substring(path.lastIndexOf('/') + 1);
        attributes.put("idConversa", Long.parseLong(idConversaStr));

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private String extrairToken(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] par = param.split("=");
            if (par.length == 2 && par[0].equals("token")) return par[1];
        }
        return null;
    }
}
