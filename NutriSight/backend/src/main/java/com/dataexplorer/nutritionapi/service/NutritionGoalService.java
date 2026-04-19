package com.dataexplorer.nutritionapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataexplorer.nutritionapi.model.NutritionGoal;
import com.dataexplorer.nutritionapi.repository.NutritionGoalRepository;

@Service
public class NutritionGoalService {
    
    @Autowired
    private NutritionGoalRepository goalRepository;
    
    /**
     * Create a new nutrition goal for a user
     */
    public NutritionGoal createGoal(NutritionGoal goal) {
        // Deactivate any existing active goals for this user
        Optional<NutritionGoal> existingActiveGoal = goalRepository.findByUserIdAndIsActiveTrue(goal.getUserId());
        if (existingActiveGoal.isPresent()) {
            NutritionGoal oldGoal = existingActiveGoal.get();
            oldGoal.setIsActive(false);
            goalRepository.save(oldGoal);
        }
        
        return goalRepository.save(goal);
    }
    
    /**
     * Get active goal for a user
     */
    public Optional<NutritionGoal> getActiveGoalByUserId(Long userId) {
        return goalRepository.findByUserIdAndIsActiveTrue(userId);
    }
    
    /**
     * Get all goals for a user
     */
    public List<NutritionGoal> getAllGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId);
    }
    
    /**
     * Get goal by ID
     */
    public Optional<NutritionGoal> getGoalById(Long id) {
        return goalRepository.findById(id);
    }
    
    /**
     * Update an existing goal
     */
    public NutritionGoal updateGoal(Long id, NutritionGoal updatedGoal) {
        Optional<NutritionGoal> existingGoal = goalRepository.findById(id);
        
        if (existingGoal.isEmpty()) {
            return null;
        }
        
        NutritionGoal goal = existingGoal.get();
        
        // Update fields
        if (updatedGoal.getGoalType() != null) {
            goal.setGoalType(updatedGoal.getGoalType());
        }
        if (updatedGoal.getTargetCalories() != null) {
            goal.setTargetCalories(updatedGoal.getTargetCalories());
        }
        if (updatedGoal.getTargetProtein() != null) {
            goal.setTargetProtein(updatedGoal.getTargetProtein());
        }
        if (updatedGoal.getTargetCarbs() != null) {
            goal.setTargetCarbs(updatedGoal.getTargetCarbs());
        }
        if (updatedGoal.getTargetFat() != null) {
            goal.setTargetFat(updatedGoal.getTargetFat());
        }
        if (updatedGoal.getStartDate() != null) {
            goal.setStartDate(updatedGoal.getStartDate());
        }
        if (updatedGoal.getEndDate() != null) {
            goal.setEndDate(updatedGoal.getEndDate());
        }
        if (updatedGoal.getIsActive() != null) {
            goal.setIsActive(updatedGoal.getIsActive());
        }
        
        return goalRepository.save(goal);
    }
    
    /**
     * Delete a goal
     */
    public boolean deleteGoal(Long id) {
        if (goalRepository.existsById(id)) {
            goalRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Deactivate a goal
     */
    public NutritionGoal deactivateGoal(Long id) {
        Optional<NutritionGoal> goal = goalRepository.findById(id);
        if (goal.isPresent()) {
            NutritionGoal g = goal.get();
            g.setIsActive(false);
            return goalRepository.save(g);
        }
        return null;
    }
}