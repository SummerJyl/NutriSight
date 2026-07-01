# nutrisight-ml

FastAPI microservice handling embedding generation for NutriSight's
semantic food search. Kept separate from the Spring Boot backend
deliberately: this service does the heavy ML matrix operations, while
Spring Boot stays the transactional, high-throughput core.

## ⚠️ Intel Mac users: run this via Docker, not a local venv

PyTorch (a dependency of `sentence-transformers`) stopped publishing
macOS **Intel (x86_64)** wheels after `torch==2.2.2`, and that version
only supports Python <=3.11. There is no combination of a recent torch
version and Python 3.13 that installs natively on an Intel Mac --
`pip install -r requirements.txt` will fail on `torch` no matter what
version you pin in `requirements.txt`.

Apple Silicon (M1/M2/M3/M4) Macs aren't affected -- this is specifically
an Intel-chip limitation. Check which one you have:

```bash
python3 -c "import platform; print(platform.machine())"
# arm64   -> Apple Silicon, local venv works fine, skip to "Setup" below
# x86_64  -> Intel, use Docker (see below) instead of a local venv
```

Full root-cause writeup: see `TROUBLESHOOTING.md`.

### Running via Docker (recommended, required on Intel Mac)

From the project root (one level up, where `docker-compose.yml` lives):

```bash
docker-compose up -d --build
```

This builds `nutrisight-ml` inside a Linux container (Linux still has
modern torch wheels for any chip) and starts it alongside the Postgres +
pgvector service. Check it's healthy:

```bash
curl http://localhost:8001/health
```

**Note on ports:** Postgres runs on host port **5433**, not the default
5432 -- this project's `docker-compose.yml` and
`backend/src/main/resources/application.properties` were both changed
to 5433 because a separate, pre-existing local Postgres install was
already bound to 5432. If you don't have a conflicting local Postgres,
you can change both back to 5432, but there's no real need to.

## Architecture

```
User types "post-workout snacks" in search bar
        |
        v
React frontend
        |
        v
Spring Boot (FoodController) ----> nutrisight-ml POST /generate-embedding
        |                                  (all-MiniLM-L6-v2, 384-dim)
        v
Postgres + pgvector
  ORDER BY description_embedding <=> :query_embedding   (cosine distance)
```

Foods are embedded once, ahead of time (see `scripts/backfill_embeddings.py`).
Only the user's search query is embedded at request time, which is why
`/generate-embedding` is a single-text endpoint rather than a batch one --
the batch path matters for backfills, not live search.

## Setup (Apple Silicon / Linux -- local venv)

```bash
pip install -r requirements.txt --break-system-packages   # or use a venv
uvicorn main:app --reload --port 8001
```

The first request that touches the embedding model will download
`all-MiniLM-L6-v2` from Hugging Face (~90MB) and cache it locally. In the
Docker setup, this download happens at build time instead (see
`Dockerfile`), so the container's first real request isn't the one
paying that cost.

## Endpoints

- `GET /health` -- confirms the embedding model is loaded and reports its dimensionality
- `POST /generate-embedding` -- `{"text": "..."}` -> `{"embedding": [...], "dimensions": 384}`
- `GET /glucose/analysis` -- unrelated legacy endpoint, untouched

## Backfilling embeddings

After running the Spring Boot Flyway migrations (which add
`foods.description_embedding`) and loading food data:

**If running via Docker:**

```bash
docker-compose exec nutrisight-ml python scripts/backfill_embeddings.py --dry-run
docker-compose exec nutrisight-ml python scripts/backfill_embeddings.py
```

**If running via local venv (Apple Silicon / Linux):**

```bash
export NUTRISIGHT_DB_DSN="dbname=nutrisight user=nutrisight password=nutrisight_dev_password host=localhost port=5433"
python scripts/backfill_embeddings.py --dry-run   # sanity check first
python scripts/backfill_embeddings.py
```

Re-run this any time new foods are synced from USDA without embeddings --
it only processes rows where `description_embedding IS NULL`.

## Known issue: Docker build hangs/fails on `pandas` (unresolved as of this writing)

Even from inside a Linux container with a recent `pip`, `pip install -r
requirements.txt` has been observed taking 200-300+ seconds and ultimately
failing with `error: metadata-generation-failed` / `pandas`, despite a
prebuilt `manylinux2014_x86_64` wheel existing for `pandas==2.2.3` on
Python 3.13. Upgrading pip first (already in the Dockerfile) reduced the
failure time from 2+ hours to under 5 minutes, but didn't fix it outright.

Not yet root-caused. Worth trying next: pinning `pandas` to an even more
recent patch version, checking `docker build --no-cache --progress=plain`
output specifically for whether pip is resolving the wheel tag correctly,
or temporarily dropping `pandas`/`numpy` from `requirements.txt` (the
embedding service itself doesn't use either -- they're only needed by
the legacy `/glucose/analysis` endpoint) to unblock the RAG-relevant part
of the service while this gets debugged separately.
're only needed by
the legacy `/glucose/analysis` endpoint) to unblock the RAG-relevant part
of the service while this gets debugged separately.
