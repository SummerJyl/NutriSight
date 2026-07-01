import json
import logging
import os
import time

from fastapi import FastAPI, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

from models.glucose import analyze_glucose
from models.embedding import encode, embedding_dimensions

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s %(levelname)s %(name)s :: %(message)s",
)
logger = logging.getLogger("nutrisight_ml")

app = FastAPI(title="NutriSight ML API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://localhost:5500"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.middleware("http")
async def log_requests(request: Request, call_next):
    """
    Request-path timing/logging. The point isn't the log line itself --
    it's that when something in the RAG chain is slow or failing, this is
    what lets you tell "the embedding call took 4 seconds" apart from
    "the Postgres vector query took 4 seconds" apart from "the request
    never even reached this service." Flat application logs with no
    request correlation can't answer that; this can.
    """
    start = time.monotonic()
    response = await call_next(request)
    duration_ms = (time.monotonic() - start) * 1000
    logger.info(
        "%s %s -> %s (%.1fms)",
        request.method, request.url.path, response.status_code, duration_ms,
    )
    return response


class EmbeddingRequest(BaseModel):
    text: str = Field(..., min_length=1, description="Text to embed")


class EmbeddingResponse(BaseModel):
    embedding: list[float]
    dimensions: int


@app.get("/")
def root():
    return {"message": "NutriSight ML API is running"}


@app.get("/health")
def health():
    """
    Separate from '/' on purpose -- this is what a deploy/orchestration
    system (or the Spring Boot side) should poll to decide if the service
    is actually ready to serve embedding requests, not just that the
    process is up.
    """
    try:
        dims = embedding_dimensions()
        return {"status": "ok", "embedding_dimensions": dims}
    except Exception as exc:
        logger.exception("Health check failed -- embedding model not ready")
        raise HTTPException(status_code=503, detail=f"Model not ready: {exc}")


@app.post("/generate-embedding", response_model=EmbeddingResponse)
def generate_embedding(payload: EmbeddingRequest):
    try:
        vector = encode(payload.text)
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc))
    except Exception:
        logger.exception("Embedding generation failed for input text")
        raise HTTPException(status_code=500, detail="Embedding generation failed")

    return EmbeddingResponse(embedding=vector, dimensions=len(vector))


@app.get("/glucose/analysis")
def glucose_analysis():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(base_dir, "data", "glucose-readings.json")
    try:
        with open(file_path) as f:
            data = json.load(f)
    except FileNotFoundError:
        raise HTTPException(status_code=404, detail="No glucose data available")

    return analyze_glucose(data)
