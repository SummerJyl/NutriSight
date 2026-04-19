import React, { useState, useMemo } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from 'recharts';

interface Nutrient {
  nutrientName: string;
  value: number;
  unitName: string;
}

interface NutrientChartProps {
  nutrients: Nutrient[];
  title?: string;
}

// Nutrient categories for filtering
const nutrientCategories = {
  all: [] as string[], // Empty means show all
  macros: ['Protein', 'Total lipid (fat)', 'Carbohydrate, by difference'],
  vitamins: [
    'Vitamin A, RAE',
    'Vitamin A, IU',
    'Vitamin B-6',
    'Vitamin B-12',
    'Vitamin C, total ascorbic acid',
    'Vitamin D',
    'Vitamin D (D2 + D3)',
    'Vitamin E (alpha-tocopherol)',
    'Vitamin K (phylloquinone)',
    'Folate, total',
    'Folic acid',
    'Niacin',
    'Riboflavin',
    'Thiamin',
  ],
  minerals: [
    'Calcium, Ca',
    'Iron, Fe',
    'Magnesium, Mg',
    'Phosphorus, P',
    'Potassium, K',
    'Sodium, Na',
    'Zinc, Zn',
    'Copper, Cu',
    'Manganese, Mn',
    'Selenium, Se',
  ],
};

type FilterType = keyof typeof nutrientCategories;

const NutrientChart: React.FC<NutrientChartProps> = ({ nutrients, title }) => {
  const [activeFilter, setActiveFilter] = useState<FilterType>('all');

  // Filter and prepare chart data
  const chartData = useMemo(() => {
    let filtered = nutrients.filter((n) => typeof n.value === 'number' && !isNaN(n.value) && n.value > 0);
    
    // Apply category filter
    if (activeFilter !== 'all' && nutrientCategories[activeFilter].length > 0) {
      filtered = filtered.filter((n) => nutrientCategories[activeFilter].includes(n.nutrientName));
    }
    
    // Sort by value descending and take top 15
    return filtered
      .sort((a, b) => b.value - a.value)
      .slice(0, 15)
      .map((n) => ({
        name: n.nutrientName,
        value: n.value,
        unit: n.unitName,
      }));
  }, [nutrients, activeFilter]);

  const filterButtons: { key: FilterType; label: string }[] = [
    { key: 'all', label: 'All' },
    { key: 'macros', label: 'Macros' },
    { key: 'vitamins', label: 'Vitamins' },
    { key: 'minerals', label: 'Minerals' },
  ];

  if (nutrients.length === 0) return null;

  return (
    <div style={{ marginBottom: '2rem' }}>
      {title && <h4 style={{ textAlign: 'center', marginBottom: '1rem' }}>{title}</h4>}
      
      {/* Filter Buttons */}
      <div style={{
        display: 'flex',
        gap: '0.5rem',
        marginBottom: '1rem',
        justifyContent: 'center',
        flexWrap: 'wrap'
      }}>
        {filterButtons.map((btn) => (
          <button
            key={btn.key}
            onClick={() => setActiveFilter(btn.key)}
            style={{
              padding: '0.5rem 1rem',
              backgroundColor: activeFilter === btn.key ? '#007bff' : '#f8f9fa',
              color: activeFilter === btn.key ? 'white' : '#495057',
              border: activeFilter === btn.key ? 'none' : '1px solid #dee2e6',
              borderRadius: '20px',
              cursor: 'pointer',
              fontSize: '0.85rem',
              fontWeight: '500',
              transition: 'all 0.2s'
            }}
            onMouseEnter={(e) => {
              if (activeFilter !== btn.key) {
                e.currentTarget.style.backgroundColor = '#e7f3ff';
              }
            }}
            onMouseLeave={(e) => {
              if (activeFilter !== btn.key) {
                e.currentTarget.style.backgroundColor = '#f8f9fa';
              }
            }}
          >
            {btn.label}
          </button>
        ))}
      </div>

      {chartData.length === 0 ? (
        <p style={{ textAlign: 'center', color: '#666', padding: '2rem' }}>
          No {activeFilter !== 'all' ? activeFilter : 'nutrient'} data available
        </p>
      ) : (
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 60 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis 
              dataKey="name" 
              angle={-45}
              textAnchor="end"
              height={100}
              interval={0}
              style={{ fontSize: '0.75rem' }}
            />
            <YAxis />
            <Tooltip 
              formatter={(value: number) => [`${value}`, 'Amount']}
              labelFormatter={(label: string) => `Nutrient: ${label}`}
            />
            <Bar dataKey="value" fill="#82ca9d" />
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default NutrientChart;