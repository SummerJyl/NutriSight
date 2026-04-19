package com.dataexplorer.nutritionapi.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "food_name", nullable = false)
    private String foodName;
    
    @Column(name = "usda_id")
    private String usdaId;
    
    @Column(name = "food_category")
    private String foodCategory;
    
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FoodNutrient> foodNutrients;
    
    // Constructors
    public Food() {}
    
    public Food(String foodName, String usdaId, String foodCategory) {
        this.foodName = foodName;
        this.usdaId = usdaId;
        this.foodCategory = foodCategory;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    
    public String getUsdaId() { return usdaId; }
    public void setUsdaId(String usdaId) { this.usdaId = usdaId; }
    
    public String getFoodCategory() { return foodCategory; }
    public void setFoodCategory(String foodCategory) { this.foodCategory = foodCategory; }
    
    public Set<FoodNutrient> getFoodNutrients() { return foodNutrients; }
    public void setFoodNutrients(Set<FoodNutrient> foodNutrients) { this.foodNutrients = foodNutrients; }
}
