"""
Loads the all-MiniLM-L6-v2 sentence-transformer model once at process
startup and exposes a single encode() function.

all-MiniLM-L6-v2 was chosen over a larger model deliberately: it produces
384-dimensional embeddings (small enough to keep pgvector index size and
query latency reasonable at this scale) and runs fast enough on CPU that
this service doesn't need a GPU to serve requests -- which matters since
this is a lightweight microservice, not a dedicated inference cluster.
"""

import logging
import time

from sentence_transformers import SentenceTransformer

logger = logging.getLogger("nutrisight_ml.embedding")

_MODEL_NAME = "all-MiniLM-L6-v2"
_model: SentenceTransformer | None = None


def get_model() -> SentenceTransformer:
    global _model
    if _model is None:
        logger.info("Loading embedding model '%s'...", _MODEL_NAME)
        start = time.monotonic()
        _model = SentenceTransformer(_MODEL_NAME)
        logger.info("Model loaded in %.2fs", time.monotonic() - start)
    return _model


def embedding_dimensions() -> int:
    return get_model().get_sentence_embedding_dimension()


def encode(text: str) -> list[float]:
    if not text or not text.strip():
        raise ValueError("Cannot generate an embedding for empty text")

    vector = get_model().encode(text, normalize_embeddings=True)
    return vector.tolist()


def encode_batch(texts: list[str]) -> list[list[float]]:
    """
    Batched encoding for backfill jobs. Encoding in batches rather than
    one HTTP round-trip per row is the difference between a backfill of
    a few thousand foods taking seconds vs. minutes -- the model can
    vectorize the whole batch as one matrix operation instead of paying
    per-call Python/model overhead thousands of times over.
    """
    cleaned = [t.strip() for t in texts if t and t.strip()]
    if not cleaned:
        return []

    vectors = get_model().encode(cleaned, normalize_embeddings=True, batch_size=64, show_progress_bar=False)
    return [v.tolist() for v in vectors]
