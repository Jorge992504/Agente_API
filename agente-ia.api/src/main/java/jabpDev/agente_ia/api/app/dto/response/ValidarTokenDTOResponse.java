package jabpDev.agente_ia.api.app.dto.response;

public record ValidarTokenDTOResponse(
        boolean isValid,
        UsuarioDTOResponse usuario
) {
}
