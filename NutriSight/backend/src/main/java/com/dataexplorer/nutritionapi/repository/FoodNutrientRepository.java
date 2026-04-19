package com.dataexplorer.nutritionapi.repository;

import com.dataexplorer.nutritionapi.model.FoodNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodNutrientRepository extends JpaRepository<FoodNutrient, Long> {
    List<FoodNutrient> findByFoodId(Long foodId);
    List<FoodNutrient> findByNutrientId(Long nutrientId);
    FoodNutrient findByFoodIdAndNutrientId(Long foodId, Long nutrientId);
}
