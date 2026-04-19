import { useEffect, useState } from "react";

interface GlucoseAnalysis {
  average: number;
  max: number;
  min: number;
  spikes: number;
  lows: number;
  by_time_of_day: {
    morning?: number;
    afternoon?: number;
    evening?: number;
    night?: number;
  };
}

export default function GlucoseInsights() {
  const [data, setData] = useState<GlucoseAnalysis | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch("http://127.0.0.1:8000/glucose/analysis")
      .then((res) => res.json())
      .then((data) => {
        setData(data);
        setLoading(false);
      })
      .catch(() => {
        setError("Could not load glucose insights");
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading insights...</p>;
  if (error) return <p>{error}</p>;
  if (!data) return null;

  return (
    <div className="glucose-insights">
      <h2>Glucose Insights</h2>
      <div className="stats">
        <div>
          <strong>Average:</strong> {data.average}
        </div>
        <div>
          <strong>Max:</strong> {data.max}
        </div>
        <div>
          <strong>Min:</strong> {data.min}
        </div>
        <div>
          <strong>Spikes:</strong> {data.spikes}
        </div>
        <div>
          <strong>Lows:</strong> {data.lows}
        </div>
      </div>
      <h3>By Time of Day</h3>
      <div className="time-of-day">
        {Object.entries(data.by_time_of_day).map(([time, value]) => (
          <div key={time}>
            <strong>{time}:</strong> {value}
          </div>
        ))}
      </div>
    </div>
  );
}
