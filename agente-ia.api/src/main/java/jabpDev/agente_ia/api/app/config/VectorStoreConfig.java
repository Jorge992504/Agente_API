package jabpDev.agente_ia.api.app.config;

import com.openai.models.vectorstores.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

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
    @Primary
    public PgVectorStore vectorStore() {

        PgVectorStore pgVectorStore = new PgVectorStore() {
            @Override
            public void add(List<Document> documents) {
                // Mock: não faz nada
            }

            @Override
            public void accept(List<Document> documents) {
                // Mock: não faz nada
            }

            @Override
            public List<Document> similaritySearch(String query) {
                return Collections.emptyList(); // Retorna vazio sem dar erro
            }

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return Collections.emptyList(); // Retonar vazio sem dar erro
            }
        };
        return pgVectorStore;
    }
}