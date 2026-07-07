package jabpDev.agente_ia.api.app.services;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemoriaIAService {

    private VectorStore vectorStore;

    public MemoriaIAService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void salvarMemoria(Long idChat, Long idMensagem, String remetente, String texto){
        if (texto == null || texto.trim().isEmpty())return;

        Document doc = new Document(texto, Map.of(
                "idChat", idChat.toString(),
                "idMensagem", idMensagem.toString(),
                "remetente", remetente
        ));
        vectorStore.add(List.of(doc));
    }

    public List<String> buscarContextoRelevante(Long idChat, String pergunta){
        SearchRequest request = SearchRequest.builder()
                .query(pergunta)
                .topK(4)
                .filterExpression("idChat == " + idChat)
                .build();

//        return vectorStore.similaritySearch(request).stream().map(Document::getText).toList();
        return List.of();
    }
}
