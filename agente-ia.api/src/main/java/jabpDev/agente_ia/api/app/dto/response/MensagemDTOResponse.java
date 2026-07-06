package jabpDev.agente_ia.api.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record MensagemDTOResponse(
        Long id,
        String remetente,
        String texto,
        String foto,
        String arquivo,
        String audio,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime criadoEm
) {
}
