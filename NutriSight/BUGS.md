# BUGS.md — nutrisight-ml

---

## [2026-06-30] Docker build failing — pandas wheel + torch no matching distribution

**Status:** ✅ Resolved

### Symptoms

- `docker compose build nutrisight-ml` failing during `pip install -r requirements.txt`
- Initial error: pandas metadata generation failure
- After pandas removed: `ERROR: Could not find a version that satisfies the requirement torch>=1.11.0 (from sentence-transformers) (from versions: none)`

### Root Causes

**1. pandas/numpy pinned in requirements.txt**
`pandas==2.2.2` and `numpy==1.26.4` caused pip metadata generation failure inside the container on Intel Mac (x86_64). The legacy `/glucose/analysis` endpoint was the only consumer of these packages.

**2. Python 3.13 base image — no PyTorch wheels**
`FROM python:3.13-slim` has no published PyTorch wheels (`cp313`). `sentence-transformers` depends on `torch>=1.11.0`, so pip returned `from versions: none` — not an incompatibility, a complete absence of any candidate wheel.

### Fixes

**1. Rewrote `models/glucose.py` — removed pandas/numpy dependency entirely**

Replaced DataFrame-based aggregations with pure Python (`datetime`, `defaultdict`, list comprehensions). Same output shape, no external dependencies.

**2. Removed `pandas` and `numpy` from `requirements.txt`**

`numpy` still comes in transitively via `sentence-transformers` at a compatible unpinned version — no need to pin it explicitly.

**3. Downgraded base image to `python:3.12-slim`**

Python 3.12 has mature, stable PyTorch wheel coverage. 3.13 does not (as of June 2026).

```dockerfile
# Before
FROM python:3.13-slim

# After
FROM python:3.12-slim
```

### Verified

```bash
curl -X POST http://localhost:8001/generate-embedding \
  -H "Content-Type: application/json" \
  -d '{"text": "high protein foods for muscle building"}'

# Returns: {"embedding": [...], "dimensions": 384} ✅
```

### Notes

- Intel Mac (x86_64) + Python 3.13 is a known bad combo for PyTorch as of mid-2026
- If upgrading to Python 3.13 in future, verify PyTorch wheel availability first at <https://download.pytorch.org/whl/>
- `pandas`/`numpy` can be restored for the glucose endpoint by adding a `requirements-legacy.txt` if needed later

---
