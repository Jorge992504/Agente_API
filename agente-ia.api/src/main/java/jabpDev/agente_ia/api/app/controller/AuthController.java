package jabpDev.agente_ia.api.app.controller;

import jabpDev.agente_ia.api.app.dto.request.LoginDTORequest;
import jabpDev.agente_ia.api.app.dto.request.RegisterDTORequest;
import jabpDev.agente_ia.api.app.dto.response.LoginDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.ValidarTokenDTOResponse;
import jabpDev.agente_ia.api.app.services.AuthService;
import jabpDev.agente_ia.api.app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginDTOResponse> register(@RequestBody RegisterDTORequest dto) {
        return ResponseEntity.ok(authService.cadastrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTOResponse> login(@RequestBody LoginDTORequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidarTokenDTOResponse> validate() {
        return ResponseEntity.ok(authService.validar(AuthUtils.getUsuarioLogado()));
    }
}
