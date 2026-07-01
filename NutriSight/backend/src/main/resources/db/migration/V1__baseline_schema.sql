-- V1: Baseline schema, matching the existing JPA entities.
-- Previously the app relied on spring.jpa.hibernate.ddl-auto=create-drop
-- against an in-memory H2 database, so nothing here persisted between
-- restarts. This migration creates the real, persistent schema in Postgres.

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP
);

CREATE TABLE foods (
    id             BIGSERIAL PRIMARY KEY,
    food_name      VARCHAR(255) NOT NULL,
    usda_id        VARCHAR(64),
    food_category  VARCHAR(100)
);

CREATE INDEX idx_foods_food_name ON foods (lower(food_name));
CREATE INDEX idx_foods_usda_id ON foods (usda_id);

CREATE TABLE nutrients (
    id                 BIGSERIAL PRIMARY KEY,
    nutrient_name      VARCHAR(255) NOT NULL,
    unit               VARCHAR(32) NOT NULL,
    nutrient_category  VARCHAR(100)
);

CREATE TABLE food_nutrients (
    id           BIGSERIAL PRIMARY KEY,
    food_id      BIGINT NOT NULL REFERENCES foods(id) ON DELETE CASCADE,
    nutrient_id  BIGINT NOT NULL REFERENCES nutrients(id) ON DELETE CASCADE,
    amount       NUMERIC(10,3) NOT NULL,
    unit         VARCHAR(32)
);

CREATE INDEX idx_food_nutrients_food_id ON food_nutrients (food_id);
CREATE INDEX idx_food_nutrients_nutrient_id ON food_nutrients (nutrient_id);

CREATE TABLE nutrition_goals (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    goal_type       VARCHAR(16) NOT NULL,
    target_calories INTEGER,
    target_protein  INTEGER,
    target_carbs    INTEGER,
    target_fat      INTEGER,
    start_date      DATE NOT NULL,
    end_date        DATE,
    is_active       BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP
);

CREATE INDEX idx_nutrition_goals_user_active ON nutrition_goals (user_id, is_active);
