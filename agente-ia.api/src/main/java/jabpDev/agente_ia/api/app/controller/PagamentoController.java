package jabpDev.agente_ia.api.app.controller;

import jabpDev.agente_ia.api.app.dto.request.CriarPagamentoDTORequest;
import jabpDev.agente_ia.api.app.dto.response.PagamentoDTOResponse;
import jabpDev.agente_ia.api.app.services.PagamentoService;
import jabpDev.agente_ia.api.app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoController {
    private PagamentoService pagamentoService;

    @PostMapping("/checkout")
    public ResponseEntity<PagamentoDTOResponse> checkout(@RequestBody CriarPagamentoDTORequest dto) {
        String idUsuario = AuthUtils.getUsuarioLogado() ;
        return ResponseEntity.ok(pagamentoService.criarCheckout(idUsuario, dto.idPlano()));
    }

    // Rota pública: o Mercado Pago chama sozinho, sem token de usuário
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) {
        pagamentoService.processarWebhook(payload);
        return ResponseEntity.ok().build();
    }
}
