package com.dataexplorer.nutritionapi.controller;

import com.dataexplorer.nutritionapi.model.Nutrient;
import com.dataexplorer.nutritionapi.service.NutrientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nutrients")
public class NutrientController {
    
    @Autowired
    private NutrientService nutrientService;
    
    // GET /api/nutrients
    @GetMapping
    public ResponseEntity<List<Nutrient>> getAllNutrients() {
        List<Nutrient> nutrients = nutrientService.getAllNutrients();
        return ResponseEntity.ok(nutrients);
    }
    
    // GET /api/nutrients?category=protein
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Nutrient>> getNutrientsByCategory(@PathVariable String category) {
        List<Nutrient> nutrients = nutrientService.getNutrientsByCategory(category);
        return ResponseEntity.ok(nutrients);
    }
    
    // GET /api/nutrients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Nutrient> getNutrientById(@PathVariable Long id) {
        Optional<Nutrient> nutrient = nutrientService.getNutrientById(id);
        return nutrient.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
}
