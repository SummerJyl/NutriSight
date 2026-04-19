package com.dataexplorer.nutritionapi.controller;

import com.dataexplorer.nutritionapi.model.Food;
import com.dataexplorer.nutritionapi.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    
    @Autowired
    private FoodService foodService;
    
    // GET /api/foods/search?query=apple&limit=10
    @GetMapping("/search")
    public ResponseEntity<Page<Food>> searchFoods(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Page<Food> foods = foodService.searchFoods(query, page, limit);
        return ResponseEntity.ok(foods);
    }
    
    // GET /api/foods/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Optional<Food> food = foodService.getFoodById(id);
        return food.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/foods?category=fruits
    @GetMapping
    public ResponseEntity<List<Food>> getFoods(
            @RequestParam(required = false) String category) {
        
        if (category != null) {
            List<Food> foods = foodService.getFoodsByCategory(category);
            return ResponseEntity.ok(foods);
        } else {
            List<Food> foods = foodService.getAllFoods();
            return ResponseEntity.ok(foods);
        }
    }
    
    // POST /api/foods (for adding new foods)
    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        Food savedFood = foodService.saveFood(food);
        return ResponseEntity.ok(savedFood);
    }
}
