package jabpDev.agente_ia.api.app.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PlanoDTOResponse(
        Long id,
        String nome,
        BigDecimal preco,
        List<String> recursos,
        Boolean destaque
) {
}
