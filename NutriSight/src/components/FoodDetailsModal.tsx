import React from "react";
import type { NutrientDetail } from "../api/usdaApi";
import NutrientChart from "./NutrientChart";
import MacroPieChart from "./MacroPieChart";

interface FoodDetailsModalProps {
  isOpen: boolean;
  onClose: () => void;
  foodName: string;
  nutrients: NutrientDetail[] | null;
  loading: boolean;
  selectedFilters: string[];
}

const FoodDetailsModal: React.FC<FoodDetailsModalProps> = ({
  isOpen,
  onClose,
  foodName,
  nutrients,
  loading,
  selectedFilters = [], // Default to empty array
}) => {
  // 1. Exit immediately if not open
  if (!isOpen) return null;

  // 2. Wrap filter in a try-catch to prevent a silent crash
  let displayedNutrients: NutrientDetail[] = [];
  try {
    displayedNutrients = (nutrients || []).filter((n) => {
      // If no filters selected, show all
      if (!selectedFilters || selectedFilters.length === 0) return true;

      const name = n.nutrientName?.toLowerCase() || "";

      if (selectedFilters.includes("High Protein") && name.includes("protein"))
        return true;
      if (
        selectedFilters.includes("High Carbs") &&
        name.includes("carbohydrate")
      )
        return true;
      if (selectedFilters.includes("High Fat") && name.includes("total lipid"))
        return true;
      if (selectedFilters.includes("Low Calorie") && name.includes("energy"))
        return true;

      return false;
    });
  } catch (err) {
    console.error("Filter logic failed:", err);
    displayedNutrients = nutrients || []; // Fallback to all nutrients
  }

  // 3. Return the UI
  return (
    <>
      {/* Backdrop */}
      <div
        onClick={onClose}
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0, 0, 0, 0.5)",
          zIndex: 999,
        }}
      />

      {/* Modal */}
      <div
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          backgroundColor: "white",
          borderRadius: "12px",
          padding: "2rem",
          maxWidth: "900px",
          width: "90%",
          maxHeight: "80vh",
          overflowY: "auto",
          zIndex: 1000,
          boxShadow: "0 20px 60px rgba(0, 0, 0, 0.3)",
        }}
      >
        {/* Header */}
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "1.5rem",
          }}
        >
          <h2 style={{ margin: 0, fontSize: "1.5rem" }}>
            {foodName} - Nutrient Analysis
          </h2>
          <button
            onClick={onClose}
            style={{
              background: "none",
              border: "none",
              fontSize: "2rem",
              cursor: "pointer",
              color: "#666",
              lineHeight: 1,
            }}
          >
            ×
          </button>
        </div>

        {/* Content */}
        {loading ? (
          <p>Loading nutrients...</p>
        ) : nutrients && nutrients.length > 0 ? (
          <div>
            <div style={{ marginBottom: "2rem" }}>
              <h3>Macronutrient Breakdown</h3>
              <MacroPieChart nutrients={nutrients} />
            </div>

            <div>
              <h3>All Nutrients</h3>
              <NutrientChart nutrients={displayedNutrients} />
            </div>
          </div>
        ) : (
          <p>No nutrients found.</p>
        )}
      </div>
    </>
  );
};

export default FoodDetailsModal;
