package jabpDev.agente_ia.api.app.utils;


import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AuthUtils {

    public static String getUsuarioLogado() {
        return (String) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }
}
