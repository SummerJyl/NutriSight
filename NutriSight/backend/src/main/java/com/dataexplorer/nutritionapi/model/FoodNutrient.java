package com.dataexplorer.nutritionapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "food_nutrients")
public class FoodNutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_id", nullable = false)
    private Nutrient nutrient;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 3)
    private BigDecimal amount;
    
    @Column(name = "unit")
    private String unit;
    
    // Constructors
    public FoodNutrient() {}
    
    public FoodNutrient(Food food, Nutrient nutrient, BigDecimal amount, String unit) {
        this.food = food;
        this.nutrient = nutrient;
        this.amount = amount;
        this.unit = unit;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
    
    public Nutrient getNutrient() { return nutrient; }
    public void setNutrient(Nutrient nutrient) { this.nutrient = nutrient; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
