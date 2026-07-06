package jabpDev.agente_ia.api.app.dto.request;

public record MensagemDTORequest(
        String type,
        Long idConversa,
        String message,
        String foto,
        String arquivo,
        String audio
) {
}
