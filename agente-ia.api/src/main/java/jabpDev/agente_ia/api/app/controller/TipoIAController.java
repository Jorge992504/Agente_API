package jabpDev.agente_ia.api.app.controller;

import jabpDev.agente_ia.api.app.dto.request.EscolherTipoIADTORequest;
import jabpDev.agente_ia.api.app.dto.response.TipoIADTOResponse;
import jabpDev.agente_ia.api.app.dto.response.UsuarioDTOResponse;
import jabpDev.agente_ia.api.app.services.TipoIAService;
import jabpDev.agente_ia.api.app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TipoIAController {

    private TipoIAService tipoIAService;

    @GetMapping("/ia/tipos")
    public ResponseEntity<List<TipoIADTOResponse>> listar() {
        return ResponseEntity.ok(tipoIAService.listar());
    }

    @PostMapping("/usuario/ia")
    public ResponseEntity<Map<String, UsuarioDTOResponse>> escolher(@RequestBody EscolherTipoIADTORequest dto) {
        String idUsuario = AuthUtils.getUsuarioLogado();
        return ResponseEntity.ok(Map.of("usuario", tipoIAService.escolherTipos(idUsuario, dto.idTipoIA())));
    }
}
