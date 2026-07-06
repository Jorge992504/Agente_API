package jabpDev.agente_ia.api.app.services;

import jabpDev.agente_ia.api.app.dto.response.ErrorDTOResponse;
import jabpDev.agente_ia.api.exception.ErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorDTOResponse> handleBusinessException(ErrorException e) {
        ErrorDTOResponse errorMessage = new ErrorDTOResponse(e.getMessage(), e.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTOResponse> handleGenericException(Exception e) {
        ErrorDTOResponse errorMessage = new ErrorDTOResponse("Ocorreu um erro inesperado" + "", 500);
        System.err.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
