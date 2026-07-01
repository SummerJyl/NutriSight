"""
Backfills foods.description_embedding for every row that doesn't have one
yet. Run this once after the V2 migration, and again any time new foods
are synced in from USDA without embeddings.

Usage:
    python scripts/backfill_embeddings.py [--batch-size 200] [--dry-run]

Why this exists as a standalone script rather than an HTTP endpoint:
backfilling thousands of rows through a request/response embedding
endpoint means thousands of round trips. This connects directly to
Postgres and calls the model in batches, which is the actual point of
having a batched encode_batch() function in models/embedding.py.
"""

import argparse
import logging
import os
import sys
import time

import psycopg2
from psycopg2.extras import execute_values

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from models.embedding import encode_batch  # noqa: E402

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s :: %(message)s")
logger = logging.getLogger("backfill_embeddings")

DB_DSN = os.environ.get(
    "NUTRISIGHT_DB_DSN",
    "dbname=nutrisight user=nutrisight password=nutrisight_dev_password host=localhost port=5432",
)


def fetch_unembedded_foods(conn, limit: int) -> list[tuple[int, str, str]]:
    with conn.cursor() as cur:
        cur.execute(
            """
            SELECT id, food_name, food_category
            FROM foods
            WHERE description_embedding IS NULL
            ORDER BY id
            LIMIT %s
            """,
            (limit,),
        )
        return cur.fetchall()


def build_embedding_text(food_name: str, food_category: str | None) -> str:
    # Including category gives the embedding a bit more semantic context
    # than the bare food name alone (e.g. "salmon" the fish vs. a brand
    # name), without requiring a richer description field that doesn't
    # exist on the Food entity yet.
    if food_category:
        return f"{food_name} ({food_category})"
    return food_name


def write_embeddings(conn, rows: list[tuple[int, list[float]]]) -> None:
    with conn.cursor() as cur:
        execute_values(
            cur,
            "UPDATE foods SET description_embedding = data.embedding "
            "FROM (VALUES %s) AS data (id, embedding) "
            "WHERE foods.id = data.id",
            [(food_id, embedding) for food_id, embedding in rows],
            template="(%s, %s::vector)",
        )
    conn.commit()


def run(batch_size: int, dry_run: bool) -> None:
    conn = psycopg2.connect(DB_DSN)
    total_embedded = 0
    start = time.monotonic()

    try:
        while True:
            foods = fetch_unembedded_foods(conn, batch_size)
            if not foods:
                break

            ids = [f[0] for f in foods]
            texts = [build_embedding_text(f[1], f[2]) for f in foods]

            logger.info("Embedding batch of %d foods (ids %d-%d)...", len(foods), ids[0], ids[-1])
            vectors = encode_batch(texts)

            if dry_run:
                logger.info("[dry-run] Would write %d embeddings, skipping DB write", len(vectors))
            else:
                write_embeddings(conn, list(zip(ids, vectors)))

            total_embedded += len(foods)

        elapsed = time.monotonic() - start
        logger.info("Done. Embedded %d foods in %.1fs", total_embedded, elapsed)

    finally:
        conn.close()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Backfill embeddings for foods missing one.")
    parser.add_argument("--batch-size", type=int, default=200)
    parser.add_argument("--dry-run", action="store_true", help="Generate embeddings but don't write them")
    args = parser.parse_args()

    run(batch_size=args.batch_size, dry_run=args.dry_run)
