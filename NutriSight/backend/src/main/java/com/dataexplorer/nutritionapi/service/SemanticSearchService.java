package com.dataexplorer.nutritionapi.service;

import com.dataexplorer.nutritionapi.repository.FoodVectorRepository;
import com.dataexplorer.nutritionapi.repository.FoodVectorRepository.FoodSemanticMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles semantic ("what does the user mean") food search, as opposed
 * to FoodService's exact substring search.
 *
 * Flow: user query text -> nutrisight-ml generates an embedding ->
 * Postgres/pgvector finds the closest food embeddings by cosine distance.
 *
 * This is intentionally a separate service from FoodService rather than
 * an added method on it -- semantic search has a different failure mode
 * (the ML service being unavailable) than a plain DB-backed keyword
 * search, and callers should be able to reason about that distinction.
 */
@Service
public class SemanticSearchService {

    private static final Logger log = LoggerFactory.getLogger(SemanticSearchService.class);

    private final EmbeddingClient embeddingClient;
    private final FoodVectorRepository foodVectorRepository;

    public SemanticSearchService(EmbeddingClient embeddingClient, FoodVectorRepository foodVectorRepository) {
        this.embeddingClient = embeddingClient;
        this.foodVectorRepository = foodVectorRepository;
    }

    public List<FoodSemanticMatch> search(String query, int limit) {
        long start = System.currentTimeMillis();
        float[] queryEmbedding = embeddingClient.generateEmbedding(query);
        long embeddingDurationMs = System.currentTimeMillis() - start;

        List<FoodSemanticMatch> matches = foodVectorRepository.findSimilarFoods(queryEmbedding, limit);
        long totalDurationMs = System.currentTimeMillis() - start;

        log.info("Semantic search query='{}' embeddingMs={} totalMs={} resultCount={}",
                query, embeddingDurationMs, totalDurationMs, matches.size());

        return matches;
    }
}
