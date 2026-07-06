package jabpDev.agente_ia.api.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jabpDev.agente_ia.api.app.dto.request.MensagemDTORequest;
import jabpDev.agente_ia.api.app.dto.response.MensagemDTOResponse;
import jabpDev.agente_ia.api.app.services.ChatService;
import jabpDev.agente_ia.api.exception.ErrorException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@RestController
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    public ChatWebSocketHandler(ChatService chatService, ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        MensagemDTORequest dto = objectMapper.readValue(message.getPayload(), MensagemDTORequest.class);
        try {
            String emailUsuario = (String) session.getAttributes().get("email");
            MensagemDTOResponse resposta = chatService.processarMensagem(emailUsuario, dto);
            enviarMensagem(session, dto.idConversa(), resposta);
        } catch (ErrorException e) {
            enviarErro(session, e.getMessage(), e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao processar mensagem do WebSocket: " + e.getMessage());
            enviarErro(session, "Erro ao processar sua mensagem.", 500);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // aqui dá pra logar/limpar controle de sessões ativas se precisar no futuro
    }

    private void enviarMensagem(WebSocketSession session, Long idConversa, MensagemDTOResponse msg) throws IOException {
        ObjectNode node = objectMapper.valueToTree(msg);
        node.put("type", "message");
        node.put("idConversa", idConversa);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(node)));
    }

    private void enviarErro(WebSocketSession session, String message, int code) throws IOException {
        Map<String, Object> payload = Map.of("type", "error", "message", message, "code", code);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
    }
}
