package com.dataexplorer.nutritionapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Thin client for the nutrisight-ml FastAPI service's embedding endpoint.
 *
 * Keeping this as its own class (rather than inlining the HTTP call into
 * SemanticSearchService) means the two failure domains -- "the ML service
 * is down" vs. "the vector query is malformed" -- stay separable, which
 * matters for debugging a RAG pipeline: a stuck/slow embedding call looks
 * very different from a bad query against Postgres, and conflating them
 * in logs makes root-causing production issues much slower.
 */
@Service
public class EmbeddingClient {

    private final RestClient restClient;

    public EmbeddingClient(@Value("${nutrisight.ml.base-url}") String mlBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(mlBaseUrl).build();
    }

    public float[] generateEmbedding(String text) {
        EmbeddingResponse response = restClient.post()
                .uri("/generate-embedding")
                .body(new EmbeddingRequest(text))
                .retrieve()
                .body(EmbeddingResponse.class);

        if (response == null || response.embedding() == null) {
            throw new EmbeddingServiceException("nutrisight-ml returned no embedding for input text");
        }

        List<Double> values = response.embedding();
        float[] result = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i).floatValue();
        }
        return result;
    }

    private record EmbeddingRequest(String text) {}

    private record EmbeddingResponse(List<Double> embedding, Integer dimensions) {}

    public static class EmbeddingServiceException extends RuntimeException {
        public EmbeddingServiceException(String message) {
            super(message);
        }
    }
}
