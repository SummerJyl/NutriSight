package com.dataexplorer.nutritionapi.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "nutrients")
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nutrient_name", nullable = false)
    private String nutrientName;
    
    @Column(name = "unit", nullable = false)
    private String unit;
    
    @Column(name = "nutrient_category")
    private String nutrientCategory; // protein, fat, carbs, vitamins, minerals
    
    @OneToMany(mappedBy = "nutrient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FoodNutrient> foodNutrients;
    
    // Constructors
    public Nutrient() {}
    
    public Nutrient(String nutrientName, String unit, String nutrientCategory) {
        this.nutrientName = nutrientName;
        this.unit = unit;
        this.nutrientCategory = nutrientCategory;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNutrientName() { return nutrientName; }
    public void setNutrientName(String nutrientName) { this.nutrientName = nutrientName; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getNutrientCategory() { return nutrientCategory; }
    public void setNutrientCategory(String nutrientCategory) { this.nutrientCategory = nutrientCategory; }
    
    public Set<FoodNutrient> getFoodNutrients() { return foodNutrients; }
    public void setFoodNutrients(Set<FoodNutrient> foodNutrients) { this.foodNutrients = foodNutrients; }
}
