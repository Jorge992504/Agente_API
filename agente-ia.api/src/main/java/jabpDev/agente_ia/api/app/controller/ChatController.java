package jabpDev.agente_ia.api.app.controller;

import jabpDev.agente_ia.api.app.dto.request.CriarChatDTORequest;
import jabpDev.agente_ia.api.app.dto.response.ChatDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.MensagemDTOResponse;
import jabpDev.agente_ia.api.app.services.ChatService;
import jabpDev.agente_ia.api.app.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatDTOResponse>> listar() {
        return ResponseEntity.ok(chatService.listar(AuthUtils.getUsuarioLogado()));
    }

    @GetMapping("/{id}/mensagens")
    public ResponseEntity<List<MensagemDTOResponse>> listarMensagens(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.listarMensagens(AuthUtils.getUsuarioLogado(), id));
    }

    @PostMapping
    public ResponseEntity<ChatDTOResponse> criar(@RequestBody CriarChatDTORequest dto) {
        return ResponseEntity.ok(chatService.criar(AuthUtils.getUsuarioLogado(), dto.idTipoIA()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        chatService.excluir(AuthUtils.getUsuarioLogado(), id);
        return ResponseEntity.noContent().build();
    }
}
