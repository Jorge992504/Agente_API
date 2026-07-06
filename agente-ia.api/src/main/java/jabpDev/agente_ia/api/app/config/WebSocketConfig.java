package jabpDev.agente_ia.api.app.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jabpDev.agente_ia.api.app.controller.ChatWebSocketHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat/*")
                .addInterceptors(jwtHandshakeInterceptor)
//                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
        ; // restringe no CORS de produção
    }
}
