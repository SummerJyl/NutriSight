from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import json
from models.glucose import analyze_glucose
import os

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://localhost:5500"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def root():
    return {"message": "NutriSight ML API is running"}

@app.get("/glucose/analysis")
def glucose_analysis():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(base_dir, "data", "glucose-readings.json")
    with open(file_path) as f:
        data = json.load(f)
    return analyze_glucose(data)
