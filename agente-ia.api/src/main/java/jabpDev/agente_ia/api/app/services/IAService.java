package jabpDev.agente_ia.api.app.services;

import jabpDev.agente_ia.api.app.entity.Chat;
import jabpDev.agente_ia.api.app.entity.Mensagem;
import jabpDev.agente_ia.api.app.entity.Remetente;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IAService {

    private ChatClient chatClient;

    public String gerarResposta(Chat chat,
                                List<Mensagem> historicoRecente,
                                String mensagemUsuario,
                                List<String> contextoRelevante) {

        StringBuilder system = new StringBuilder(
                chat.getTipoIA().getPromptSistema() != null
                        ? chat.getTipoIA().getPromptSistema()
                        : "Você é um assistente útil e direto."
        );

        if (contextoRelevante != null && !contextoRelevante.isEmpty()) {
            system.append("\n\nContexto de mensagens anteriores relevantes dessa conversa:\n");
            contextoRelevante.forEach(c -> system.append("- ").append(c).append("\n"));
        }

        List<Message> mensagens = new ArrayList<>();
        mensagens.add(new SystemMessage(system.toString()));

        for(Mensagem m : historicoRecente){
            if (m.getTexto() == null || m.getTexto().isBlank()) continue;
            if (m.getRemetente() == Remetente.USUARIO){
                mensagens.add(new UserMessage(m.getTexto()));
            }else{
                mensagens.add(new AssistantMessage(m.getTexto()));
            }
        }
        mensagens.add(new UserMessage(mensagemUsuario));

        return chatClient.prompt(new Prompt(mensagens))
                .system(system.toString())
                .user(mensagemUsuario)
                .call()
                .content();
    }
}
