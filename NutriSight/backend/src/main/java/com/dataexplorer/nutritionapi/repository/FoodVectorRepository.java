package com.dataexplorer.nutritionapi.repository;

import com.pgvector.PGvector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles semantic search against foods.description_embedding.
 *
 * Spring Data JPA / Hibernate doesn't understand the Postgres `vector`
 * column type out of the box, so this bypasses the JPA repository layer
 * and talks to the column directly via JdbcTemplate + pgvector-java's
 * PGvector type, which knows how to serialize a float[] into the format
 * Postgres expects.
 *
 * Cosine distance (the `<=>` operator) is used rather than L2 distance
 * because sentence-transformer embeddings are meant to be compared by
 * direction, not magnitude.
 */
@Repository
public class FoodVectorRepository {

    private final JdbcTemplate jdbcTemplate;

    public FoodVectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Stores/updates the embedding for a single food row.
     */
    public void saveEmbedding(Long foodId, float[] embedding) {
        jdbcTemplate.update(
                "UPDATE foods SET description_embedding = ? WHERE id = ?",
                ps -> bindVector(ps, 1, embedding, foodId)
        );
    }

    /**
     * Returns the top `limit` foods by cosine similarity to the given
     * query embedding. Only rows that already have an embedding are
     * eligible -- foods that haven't been backfilled yet are silently
     * excluded rather than erroring.
     */
    public List<FoodSemanticMatch> findSimilarFoods(float[] queryEmbedding, int limit) {
        String sql = """
                SELECT id, food_name, food_category, usda_id,
                       1 - (description_embedding <=> ?) AS similarity
                FROM foods
                WHERE description_embedding IS NOT NULL
                ORDER BY description_embedding <=> ?
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, ps -> {
            PGvector vector = new PGvector(queryEmbedding);
            ps.setObject(1, vector);
            ps.setObject(2, vector);
            ps.setInt(3, limit);
        }, (rs, rowNum) -> new FoodSemanticMatch(
                rs.getLong("id"),
                rs.getString("food_name"),
                rs.getString("food_category"),
                rs.getString("usda_id"),
                rs.getDouble("similarity")
        ));
    }

    public long countMissingEmbeddings() {
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM foods WHERE description_embedding IS NULL", Long.class);
    }

    private void bindVector(PreparedStatement ps, int paramIndex, float[] embedding, Long foodId) throws SQLException {
        ps.setObject(paramIndex, new PGvector(embedding));
        ps.setLong(paramIndex + 1, foodId);
    }

    public record FoodSemanticMatch(
            Long id,
            String foodName,
            String foodCategory,
            String usdaId,
            double similarity
    ) {}
}
