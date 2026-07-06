package jabpDev.agente_ia.api.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {
    private final int code;
    public ErrorException(String message, int code) {
        super(message);
        this.code = code;
    }
}
