package jabpDev.agente_ia.api.app.services;


import lombok.AllArgsConstructor;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmbeddingService {

    private OllamaEmbeddingModel embeddingModel;

    public float[] gerarEmbedding(String text){
        return embeddingModel.embed(text);
    }
}
