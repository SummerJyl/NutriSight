import pandas as pd
import numpy as np
from datetime import datetime

def analyze_glucose(data: list) -> dict:
    df = pd.DataFrame(data)
    df['timestamp'] = pd.to_datetime(df['timestamp'])
    df['hour'] = df['timestamp'].dt.hour
    df['time_of_day'] = df['hour']. apply(lambda x:
        'morning' if 6 <= x < 12
        else 'afternoon' if 12 <= x < 18
        else 'evening' if 18 <=x < 22
        else 'night'
    )

    return {
        "average": round(df['glucose'].mean(), 2),
        "max": int(df['glucose'].max()),
        "min": int(df['glucose'].min()),
        "spikes": int((df['glucose'] > 140).sum()),
        "lows": int((df['glucose'] < 70).sum()),
        "by_time_of_day": df.groupby('time_of_day')['glucose'].mean().round(2).to_dict()
    } 