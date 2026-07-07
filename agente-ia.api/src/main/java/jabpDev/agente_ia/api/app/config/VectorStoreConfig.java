package jabpDev.agente_ia.api.app.config;

import com.openai.models.vectorstores.VectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorStoreConfig {

//    @Bean
//    public PgVectorStore vectorStore(
//            JdbcTemplate jdbcTemplate,
//            @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
//
//        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
//                .build();
//    }

    @Bean
    public PgVectorStore vectorStore(
            JdbcTemplate jdbcTemplate,
            EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .build();
    }


    @Bean
    @Primary
    public EmbeddingModel localEmbeddingModel() {
        // Criando explicitamente o modelo local do Transformers
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        try {
            // Isso força o Spring AI a rodar o processo de inicialização do ONNX imediatamente
            embeddingModel.afterPropertiesSet();
        } catch (Exception e) {
            throw new IllegalStateException("Falha crítica ao inicializar o modelo ONNX local: " + e.getMessage(), e);
        }
        return embeddingModel;
    }
}
