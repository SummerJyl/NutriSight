import React from 'react';
import type { TooltipProps } from 'recharts';
import type { ValueType, NameType } from 'recharts/types/component/DefaultTooltipContent';

interface PayloadItem {
  payload: {
    name: string;
    value: number;
    unit: string;
  };
}

const CustomTooltip: React.FC<TooltipProps<ValueType, NameType>> = ({ active, payload }) => {
  if (active && payload && payload.length > 0) {
    const item = payload[0] as PayloadItem;
    return (
      <div
        className="custom-tooltip"
        style={{ background: '#fff', padding: '8px', border: '1px solid #ccc' }}
      >
        <p>{`${item.payload.name}: ${item.payload.value} ${item.payload.unit}`}</p>
      </div>
    );
  }

  return null;
};

export default CustomTooltip;
