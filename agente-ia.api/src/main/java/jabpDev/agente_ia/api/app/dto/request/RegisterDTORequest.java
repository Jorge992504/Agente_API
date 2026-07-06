package jabpDev.agente_ia.api.app.dto.request;

public record RegisterDTORequest(
        String nome,
        String email,
        String senha
) {
}
