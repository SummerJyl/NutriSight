# BHDE Nutrition Tracker - Unified Development Plan

## Project Vision

A nutrition tracking application that allows users to explore USDA food database, set personal nutrition goals, log daily food intake, and visualize progress toward their health objectives.

## Current Status

- ✅ Spring Boot backend setup
- ✅ Basic project structure (controller/model/repository/service)
- ✅ USDA food database integration
- ✅ User authentication (User entity, login/register endpoints and UI)
- ✅ React frontend with routing
- ⏳ User profile display (needs work)

## Phase 1: User Profile & Goals [CURRENT FOCUS]

### Week 1: Display Logged-In User

- ✅ Update Home.tsx to show user info when authenticated
- ✅ Add logout button
- ✅ Add navigation to login/register when not logged in
- ✅ Style user profile section

### Week 2: NutritionGoal Backend

- [ ] Create NutritionGoal entity (daily targets for calories, protein, carbs, fat)
- [ ] Build NutritionGoalRepository
- [ ] Implement NutritionGoalService (CRUD operations)
- [ ] Create NutritionGoalController endpoints
- [ ] Test with Postman

### Week 3: NutritionGoal Frontend

- ✅ Create Goals page component
- ✅ Build form to set/edit nutrition goals
- ✅ Display current goals on dashboard
- ✅ Add goal validation and error handling

## Phase 2: Enhanced Food Search & Exploration

### Week 4: Macro Filter Implementation

- ✅ Design filter UI component (4 checkboxes: High Protein, High Carbs, High Fat, Low Calorie)
- ✅ Add filter state management to Home component
- ✅ Define filter thresholds:
  - High Protein: >20g per 100g
  - High Carbs: >40g per 100g  
  - High Fat: >20g per 100g
  - Low Calorie: <100 kcal per 100g
- ✅ Implement frontend filtering logic on search results
- ✅ Display active filters as removable chips
- ✅ Add "Clear All Filters" button
- ✅ Test filter combinations

### Week 5: Search UX Improvements

- ✅ Add loading states for searches (spinner/skeleton)
- ✅ Handle empty results gracefully with helpful messaging
- ✅ Improve pagination (refine existing "Show More" button)
- [ ] Add total results count display
- [ ] Implement search result sorting (by relevance, protein, calories)
- ✅ Add food detail modal or page for full nutrient breakdown
- [ ] Style improvements for better mobile experience

### Week 6: Advanced Filtering (Optional)

- [ ] Add numeric range inputs for custom thresholds
- [ ] Implement "Save Filter Preset" functionality
- [ ] Add specific micronutrient filters if user feedback indicates need
- [ ] Filter history/recent searches

## Phase 3: Food Logging & Tracking

### Week 6: FoodLog Backend

- [ ] Create FoodLog entity (tracks what user ate)
- [ ] FoodLog repository and service
- [ ] Link FoodLog to User and FoodItem
- [ ] Create logging endpoints (add/delete/view logs)

### Week 7: FoodLog Frontend

- [ ] Add "Log This Food" button to search results
- [ ] Create food diary page showing logged foods
- [ ] Display daily totals (calories, macros)
- [ ] Add ability to delete logged foods

### Week 8: Progress Tracking

- [ ] Calculate daily totals from FoodLog
- [ ] Compare actual vs NutritionGoal
- [ ] Display progress percentages
- [ ] Add color coding (green = met goal, red = over/under)

## Phase 4: Data Visualization & Dashboard

### Week 9: Basic Charts

- ✅  Install chart library (recharts or chart.js)
- ✅  Create nutrient breakdown pie chart for single food
- ✅  Create nutrient bar chart with filtering 
- [ ] Create daily progress bar charts
- [ ] Add weekly trend line graphs

### Week 10: Dashboard Page

- [ ] Build main dashboard layout
- [ ] Summary cards with key metrics
- [ ] Quick stats (calories today, goal progress)
- [ ] Recent food logs preview

### Week 11: Advanced Visualizations

- [ ] Historical trends (past 7/30 days)
- [ ] Nutrient comparison charts
- [ ] Goal achievement streaks
- [ ] Export data functionality

## Phase 5: User Experience Enhancements

### Week 12: Saved Foods & Favorites

- [ ] Add "favorite" functionality to foods
- [ ] Create saved foods page
- [ ] Quick-add from favorites
- [ ] Manage saved searches

### Week 13: Mobile Optimization

- [ ] Responsive layout testing
- [ ] Mobile-friendly filter panel
- [ ] Touch-optimized controls
- [ ] Test on various screen sizes

### Week 14: Accessibility

- [ ] Add ARIA labels
- [ ] Keyboard navigation support
- [ ] Screen reader testing
- [ ] Color contrast compliance

## Phase 6: Polish & Testing

### Week 15: Testing

- [ ] Unit tests for backend services
- [ ] Integration tests for API endpoints
- [ ] Frontend component tests
- [ ] End-to-end user flow tests

### Week 16: Documentation

- [ ] Expand README with setup instructions
- [ ] API documentation
- [ ] Component documentation
- [ ] User guide

## Daily Practice Strategy

- **Time commitment:** 30-60 minutes per day
- **Goal:** One small task per day minimum
- **Focus:** Complete one feature fully before moving to next
- **Track:** Check off tasks as completed
- **Commit:** Push code daily to maintain momentum

## Current Sprint: Phase 1, Week 1

**Goal:** Show logged-in user info on Home page with logout capability

**This Week's Tasks:**

1. Update Home.tsx to read user from localStorage
2. Display "Welcome, [firstName]!" message
3. Add logout button that clears localStorage
4. Redirect to login if not authenticated
5. Test the complete login → home → logout flow

## User Flow (Complete Vision)

1. User logs in / creates account ✅
2. User sets nutrition goals (daily calorie/macro targets)
3. User searches USDA food database with advanced filters
4. User views detailed nutrition info for specific foods
5. User logs foods they consumed
6. App calculates daily totals and compares to goals
7. Dashboard shows progress with charts and visualizations
8. User can view historical trends and saved favorites

## Interview Talking Points

- Full-stack application with React + Spring Boot
- RESTful API design and authentication
- Complex data relationships (User → Goals → FoodLogs → FoodItems)
- Real-world integration with USDA API
- Data visualization and user analytics
- Responsive, accessible UI design
- Test-driven development practices

## Notes

- Keep commits small and focused
- Write clear commit messages
- Document design decisions
- Build features iteratively, test frequently
- Don't try to build everything at once - one feature at a time!
