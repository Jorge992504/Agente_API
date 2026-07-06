package jabpDev.agente_ia.api.app.dto.request;

import java.util.List;

public record EscolherTipoIADTORequest(
        List<Long> idTipoIA
) {
}
