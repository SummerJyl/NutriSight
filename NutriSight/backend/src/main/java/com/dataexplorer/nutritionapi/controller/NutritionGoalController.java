package com.dataexplorer.nutritionapi.controller;

import com.dataexplorer.nutritionapi.model.NutritionGoal;
import com.dataexplorer.nutritionapi.service.NutritionGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
public class NutritionGoalController {
    
    @Autowired
    private NutritionGoalService goalService;
    
    /**
     * Create a new nutrition goal
     * POST /api/goals
     */
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody NutritionGoal goal) {
        if (goal.getUserId() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "userId is required");
            return ResponseEntity.badRequest().body(error);
        }
        
        NutritionGoal created = goalService.createGoal(goal);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Get active goal for a user
     * GET /api/goals/user/{userId}/active
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<?> getActiveGoal(@PathVariable Long userId) {
        Optional<NutritionGoal> goal = goalService.getActiveGoalByUserId(userId);
        
        if (goal.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No active goal found for this user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(goal.get());
    }
    
    /**
     * Get all goals for a user
     * GET /api/goals/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllGoals(@PathVariable Long userId) {
        List<NutritionGoal> goals = goalService.getAllGoalsByUserId(userId);
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get goal by ID
     * GET /api/goals/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable Long id) {
        Optional<NutritionGoal> goal = goalService.getGoalById(id);
        
        if (goal.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Goal not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(goal.get());
    }
    
    /**
     * Update a goal
     * PUT /api/goals/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody NutritionGoal goal) {
        NutritionGoal updated = goalService.updateGoal(id, goal);
        
        if (updated == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Goal not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Delete a goal
     * DELETE /api/goals/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        boolean deleted = goalService.deleteGoal(id);
        
        if (!deleted) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Goal not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Goal deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Deactivate a goal
     * POST /api/goals/{id}/deactivate
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateGoal(@PathVariable Long id) {
        NutritionGoal deactivated = goalService.deactivateGoal(id);
        
        if (deactivated == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Goal not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(deactivated);
    }
}