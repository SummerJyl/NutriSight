// src/components/MacroPieChart.tsx
import React from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

interface MacroData {
  name: string;
  value: number;
}

interface MacroPieChartProps {
  nutrients: { nutrientName: string; value: number; unitName: string }[];
}

const COLORS = ['#82ca9d', '#8884d8', '#ffc658']; // Protein, Carbs, Fat

const MacroPieChart: React.FC<MacroPieChartProps> = ({ nutrients }) => {
  console.log('MacroPieChart nutrients:', nutrients);
  const macroNames = ['Protein', 'Carbohydrate, by difference', 'Total lipid (fat)'];

  const macroData: MacroData[] = nutrients
    .filter((n) => macroNames.includes(n.nutrientName))
    .map((n) => ({
      name: n.nutrientName,
      value: n.value,
    }));

  if (macroData.length === 0) return <p>No macronutrient data available.</p>;

  return (
    <div style={{ width: '100%', height: 300 }}>
      <h4 style={{ textAlign: 'center' }}>Macronutrient Breakdown</h4>
      <ResponsiveContainer>
        <PieChart>
          <Pie data={macroData} dataKey="value" nameKey="name" outerRadius={90} label>
            {macroData.map((_, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip />
          <Legend />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
};

export default MacroPieChart;
