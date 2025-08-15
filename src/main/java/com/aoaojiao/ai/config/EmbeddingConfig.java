package com.aoaojiao.ai.config;

import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author DD
 */
@Configuration
public class EmbeddingConfig {

    @Bean
    public BatchingStrategy customTokenCountBatchingStrategy() {
        return new TokenCountBatchingStrategy(
                EncodingType.CL100K_BASE,  // Specify the encoding type
                7000,                      // Set the maximum input token count
                0.1                        // Set the reserve percentage
        );
    }

    @Bean
    public VectorStore chromaVectorStore(EmbeddingModel embeddingModel,
                                         ChromaApi chromaApi,
                                         BatchingStrategy BatchingStrategy) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .batchingStrategy(BatchingStrategy)
                .collectionName("TestCollection")
                .initializeSchema(true)
                .build();
    }
}
