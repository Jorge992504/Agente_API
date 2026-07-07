package jabpDev.agente_ia.api.app.config;

import com.openai.models.vectorstores.VectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
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
}
