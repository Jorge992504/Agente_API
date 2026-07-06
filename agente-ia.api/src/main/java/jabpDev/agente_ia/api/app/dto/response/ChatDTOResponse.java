package jabpDev.agente_ia.api.app.dto.response;

import java.time.LocalDateTime;

public record ChatDTOResponse(
        Long id,
        String titulo,
        Long idTipoIA,
        String nomeTipoIA,
        LocalDateTime atualizado
) {
}
