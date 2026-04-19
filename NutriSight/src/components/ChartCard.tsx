import React from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import CustomTooltip from './CustomTooltip';

interface Nutrient {
  nutrientName: string;
  value: number;
  unitName: string;
}

interface ChartCardProps {
  title: string;
  data: Nutrient[];
}

const ChartCard: React.FC<ChartCardProps> = ({ title, data }) => {
  // Prepare data for chart, filter only key nutrients or top nutrients, etc.
  const chartData = data.map((n) => ({
    name: n.nutrientName,
    value: n.value,
    unit: n.unitName,
  }));

  return (
    <div>
      <h3>{title}</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip content={<CustomTooltip />} />

          <Bar dataKey="value" fill="#2c7a7b" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ChartCard;
