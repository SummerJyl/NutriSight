package com.dataexplorer.nutritionapi.service;

import com.dataexplorer.nutritionapi.model.Nutrient;
import com.dataexplorer.nutritionapi.repository.NutrientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NutrientService {
    
    @Autowired
    private NutrientRepository nutrientRepository;
    
    // Get all nutrients
    public List<Nutrient> getAllNutrients() {
        return nutrientRepository.findAll();
    }
    
    // Get nutrients by category (protein, fat, carbs, vitamins, minerals)
    public List<Nutrient> getNutrientsByCategory(String category) {
        return nutrientRepository.findByNutrientCategory(category);
    }
    
    // Get nutrient by ID
    public Optional<Nutrient> getNutrientById(Long id) {
        return nutrientRepository.findById(id);
    }
    
    // Save nutrient (for USDA sync)
    public Nutrient saveNutrient(Nutrient nutrient) {
        return nutrientRepository.save(nutrient);
    }
}
