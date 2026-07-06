package jabpDev.agente_ia.api.app.controller;

import jabpDev.agente_ia.api.app.dto.request.EscolherPlanoDTORequest;
import jabpDev.agente_ia.api.app.dto.response.PlanoDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.UsuarioDTOResponse;
import jabpDev.agente_ia.api.app.services.PlanoService;
import jabpDev.agente_ia.api.app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PlanoController {

    private final PlanoService planoService;

    @GetMapping("/planos")
    public ResponseEntity<List<PlanoDTOResponse>> listar() {
        return ResponseEntity.ok(planoService.listar());
    }

    @PostMapping("/usuario/plano")
    public ResponseEntity<Map<String, UsuarioDTOResponse>> escolher(@RequestBody EscolherPlanoDTORequest dto) {
        String idUsuario = AuthUtils.getUsuarioLogado();
        return ResponseEntity.ok(Map.of("usuario", planoService.escolherPlano(idUsuario, dto.idPlano())));
    }
}
