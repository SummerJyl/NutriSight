package com.dataexplorer.nutritionapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataexplorer.nutritionapi.model.NutritionGoal;

@Repository
public interface NutritionGoalRepository extends JpaRepository<NutritionGoal, Long> {
    
    // Find all goals for a specific user
    List<NutritionGoal> findByUserId(Long userId);
    
    // Find active goal for a user
    Optional<NutritionGoal> findByUserIdAndIsActiveTrue(Long userId);
    
    // Find goals by user and type
    List<NutritionGoal> findByUserIdAndGoalType(Long userId, com.dataexplorer.nutritionapi.model.GoalType goalType);
}