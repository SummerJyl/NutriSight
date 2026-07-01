from datetime import datetime
from collections import defaultdict


def _time_of_day(hour: int) -> str:
    if 6 <= hour < 12:
        return "morning"
    elif 12 <= hour < 18:
        return "afternoon"
    elif 18 <= hour < 22:
        return "evening"
    else:
        return "night"


def analyze_glucose(data: list) -> dict:
    if not data:
        return {
            "average": 0,
            "max": 0,
            "min": 0,
            "spikes": 0,
            "lows": 0,
            "by_time_of_day": {},
        }

    glucose_values = [row["glucose"] for row in data]
    timestamps = [datetime.fromisoformat(row["timestamp"]) for row in data]

    by_time_of_day_sums = defaultdict(list)
    for row, ts in zip(data, timestamps):
        bucket = _time_of_day(ts.hour)
        by_time_of_day_sums[bucket].append(row["glucose"])

    by_time_of_day = {
        bucket: round(sum(values) / len(values), 2)
        for bucket, values in by_time_of_day_sums.items()
    }

    return {
        "average": round(sum(glucose_values) / len(glucose_values), 2),
        "max": max(glucose_values),
        "min": min(glucose_values),
        "spikes": sum(1 for g in glucose_values if g > 140),
        "lows": sum(1 for g in glucose_values if g < 70),
        "by_time_of_day": by_time_of_day,
    }