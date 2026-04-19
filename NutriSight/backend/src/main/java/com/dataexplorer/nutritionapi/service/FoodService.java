package com.dataexplorer.nutritionapi.service;

import com.dataexplorer.nutritionapi.model.Food;
import com.dataexplorer.nutritionapi.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    
    @Autowired
    private FoodRepository foodRepository;
    
    // Search foods with caching
    @Cacheable("foodSearch")
    public Page<Food> searchFoods(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foodRepository.findByFoodNameContainingIgnoreCase(query.trim(), pageable);
    }
    
    // Get food by ID
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }
    
    // Get foods by category
    public List<Food> getFoodsByCategory(String category) {
        return foodRepository.findByFoodCategory(category);
    }
    
    // Get all foods
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    
    // Save food (for USDA sync)
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }
}
