package jabpDev.agente_ia.api.app.dto.response;

public record LoginDTOResponse(
        String token,
        UsuarioDTOResponse usuario
) {
}
