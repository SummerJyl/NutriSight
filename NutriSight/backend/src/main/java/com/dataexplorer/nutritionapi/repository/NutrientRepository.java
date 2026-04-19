package com.dataexplorer.nutritionapi.repository;

import com.dataexplorer.nutritionapi.model.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    List<Nutrient> findByNutrientCategory(String nutrientCategory);
    Nutrient findByNutrientName(String nutrientName);
}
