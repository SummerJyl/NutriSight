import React, { useState } from "react";
import type { NutrientDetail } from "../api/usdaApi";
import { fetchFoodDetails } from "../api/usdaApi";
import FoodDetailsModal from "./FoodDetailsModal";

interface Food {
  description: string;
  fdcId: number;
  dataType: string;
}

interface FoodCardProps {
  food: Food;
  selectedFilters: string[];
}

const FoodCard: React.FC<FoodCardProps> = ({ food, selectedFilters }) => {
  const [detailedNutrients, setDetailedNutrients] = useState<
    NutrientDetail[] | null
  >(null);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleClick = async () => {
    console.log("FoodCard clicked:", food.description);
    setIsModalOpen(true);

    if (!detailedNutrients) {
      setLoading(true);
      try {
        const nutrients = await fetchFoodDetails(food.fdcId);
        console.log("Fetched nutrients:", nutrients);
        setDetailedNutrients(nutrients);
      } catch (error) {
        console.error("Failed to fetch nutrient details", error);
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <>
      <div
        onClick={handleClick}
        style={{
          cursor: "pointer",
          border: "1px solid #ddd",
          borderRadius: "8px",
          marginBottom: 16,
          padding: 16,
          transition: "all 0.2s",
          backgroundColor: "white",
        }}
        onMouseEnter={(e) => {
          e.currentTarget.style.boxShadow = "0 4px 12px rgba(0,0,0,0.1)";
          e.currentTarget.style.transform = "translateY(-2px)";
        }}
        onMouseLeave={(e) => {
          e.currentTarget.style.boxShadow = "none";
          e.currentTarget.style.transform = "translateY(0)";
        }}
      >
        <h3 style={{ margin: "0 0 8px 0" }}>{food.description}</h3>
        <p style={{ margin: "4px 0", color: "#666" }}>FDC ID: {food.fdcId}</p>
        <p style={{ margin: "4px 0", color: "#666" }}>Type: {food.dataType}</p>
      </div>

     <FoodDetailsModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        foodName={food.description}
        nutrients={detailedNutrients}
        loading={loading}
        selectedFilters={selectedFilters}
      />
    </>
  );
};

export default FoodCard;
