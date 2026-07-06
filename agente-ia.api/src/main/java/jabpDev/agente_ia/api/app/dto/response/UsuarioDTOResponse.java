package jabpDev.agente_ia.api.app.dto.response;

import java.util.List;

public record UsuarioDTOResponse(
        Long id,
        String nome,
        String email,
        Long idPlano,
        String nomePlano,
        List<TipoIADTOResponse> tiposIA
) {
}
