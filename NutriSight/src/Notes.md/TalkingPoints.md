# Technical Decision Log - BHDE Nutrition Tracker

## Purpose
Document key technical decisions, tradeoffs, and reasoning for interview discussions. Shows thought process beyond just "making it work."

---

## Decision 1: Filter Implementation Strategy

### Context
Building nutrient filtering (High Protein, High Carbs, etc.) for food search results from USDA API.

### Options Considered

**Option A: Client-Side Filtering After Search**
- Fetch all search results from USDA API
- For each result, fetch detailed nutrient data
- Filter in frontend based on nutrient thresholds
- **Pros:** Simple implementation, no backend changes needed
- **Cons:** Multiple API calls (N+1 problem), slow performance, wastes bandwidth

**Option B: Backend-Mediated Filtering**
- Backend receives filter criteria from frontend
- Backend queries USDA API with appropriate parameters
- Returns pre-filtered results
- **Pros:** Single API call, better performance, less frontend complexity
- **Cons:** Requires backend changes, depends on USDA API supporting these filters

**Option C: Hybrid Approach**
- Fetch search results with basic nutrient summaries (if available in USDA response)
- Filter client-side on available data
- Lazy-load full details only when user clicks a food
- **Pros:** Balance of performance and accuracy, progressive enhancement
- **Cons:** Filtering accuracy depends on what USDA includes in search results

### Decision Made
[To be determined based on USDA API data structure]

### Reasoning
Need to examine actual USDA API response to see:
1. Does search endpoint include nutrient data?
2. How much data per food item?
3. Can we filter without fetching full details for every result?

### Interview Talking Point
"When implementing filtering, I evaluated multiple approaches considering performance, user experience, and API constraints. Rather than jumping to the first solution, I considered the tradeoffs between client-side and server-side filtering, including the N+1 query problem and bandwidth usage."

---

## Decision 2: Filter Categories Selection

### Context
Users need to filter foods by nutritional content to help meet their nutrition goals.

### Initial Approach
Original TODO listed: Protein, Fat, Carbs, Vitamins, Minerals (5 categories)

### Problem Identified
"Vitamins" and "Minerals" are too broad:
- "Rich in Vitamins" doesn't specify which vitamins (A, C, D, K, B-complex all serve different purposes)
- Same issue with minerals (Iron vs Calcium vs Magnesium have different use cases)
- Creates cognitive overload without practical utility

### Refined Approach
Focus on 4 macro nutrient filters:
- High Protein (>20g/100g)
- High Carbs (>40g/100g)
- High Fat (>20g/100g)
- Low Calorie (<100 kcal/100g)

### Reasoning
1. **User-Centered Design:** Most users tracking nutrition focus on macros, not specific micronutrients
2. **Actionable Filters:** Someone with a 150g protein goal can immediately use "High Protein" filter
3. **Iterative Development:** Build what's clearly useful first, add micronutrient filters if user feedback indicates need
4. **Avoid Feature Bloat:** 20+ vitamin/mineral filters would overwhelm UI without evidence users need them

### Interview Talking Point
"I critically evaluated the initial feature set rather than blindly implementing what was listed. By questioning whether 'Vitamins' as a single filter was meaningful, I identified that macro nutrients provide more practical value for the core use case. This demonstrates product thinking - building features users will actually use rather than features that just sound comprehensive."

---

## Decision 3: Default Search Query UX

### Context
Search input needs initial state when page loads.

### Options Considered
1. Pre-filled with specific food (e.g., "Vitamin D")
2. Pre-filled with common food (e.g., "chicken")  
3. Empty with placeholder text

### Decision Made
Empty input with placeholder "Search foods..."

### Reasoning
- Pre-filled values bias user's search behavior
- Users might not notice the pre-filled text and get confused by results
- Empty state with clear placeholder is cleaner UX pattern
- Lets users decide what to search for without influence

### Interview Talking Point
"Small UX decisions matter. Rather than defaulting to a specific search term, I chose an empty state because pre-filled values can confuse users or bias their behavior. This shows attention to user experience details beyond just functional requirements."

---

## Decision 4: Authentication Storage Strategy

### Context
Need to persist user session after login.

### Decision Made
LocalStorage for user data (userId, email, name)

### Tradeoffs Acknowledged
**Current Implementation (localStorage):**
- Simple to implement
- Works across page refreshes
- No server-side session management needed
- **Security limitation:** Not suitable for sensitive tokens in production

**Production Considerations:**
- Would use httpOnly cookies for auth tokens
- Implement JWT with refresh tokens
- Add CSRF protection
- Current approach is appropriate for MVP/learning project

### Interview Talking Point
"I chose localStorage for session management in this MVP, understanding it's not production-grade security. In a real application, I'd use httpOnly cookies with JWT tokens and implement proper CSRF protection. This demonstrates understanding the difference between MVP implementations and production requirements."

---

## Decision 5: Backend Architecture - Service Layer Pattern

### Context
Structuring backend code for maintainability and testability.

### Decision Made
Implemented service layer between controllers and repositories:
- Controllers handle HTTP concerns (routing, request/response)
- Services contain business logic (validation, data transformation)
- Repositories handle data access (database queries)

### Reasoning
- **Separation of Concerns:** Each layer has single responsibility
- **Testability:** Can test business logic independently of HTTP/database
- **Reusability:** Services can be called from multiple controllers
- **Maintainability:** Changes to business logic don't require controller changes

### Example: NutritionGoal Creation
```java
// Service handles business logic
public NutritionGoal createGoal(NutritionGoal goal) {
    // Deactivate existing active goals
    Optional<NutritionGoal> existingActiveGoal = 
        goalRepository.findByUserIdAndIsActiveTrue(goal.getUserId());
    if (existingActiveGoal.isPresent()) {
        NutritionGoal oldGoal = existingActiveGoal.get();
        oldGoal.setIsActive(false);
        goalRepository.save(oldGoal);
    }
    return goalRepository.save(goal);
}
```

### Interview Talking Point
"I implemented a three-tier architecture with clear separation between controllers, services, and repositories. This follows established design patterns and makes the code more maintainable and testable. For example, the NutritionGoalService handles the business rule that only one goal can be active at a time, keeping that logic out of the controller."

---

## Decision 6: Single vs Multiple Active Goals

### Context
Users can have multiple nutrition goals. Should multiple be active simultaneously?

### Decision Made
Only one active goal at a time per user.

### Reasoning
- **Prevents Confusion:** Multiple active goals would require choosing which one to track against
- **Simplifies UI:** Dashboard shows progress toward one clear target
- **Aligns with User Behavior:** People typically have one current goal, can switch as needed
- **Extensible Design:** Can add goal switching/history later if needed

### Implementation
When creating new goal, automatically deactivates previous active goal:
```java
if (existingActiveGoal.isPresent()) {
    NutritionGoal oldGoal = existingActiveGoal.get();
    oldGoal.setIsActive(false);
    goalRepository.save(oldGoal);
}
```

### Interview Talking Point
"I made an intentional design decision to allow only one active goal at a time. While the database supports multiple goals, enforcing single active status prevents user confusion and simplifies the tracking experience. This shows thinking through user experience implications of data model decisions."

---

## Template for Future Decisions

### Context
[What problem are you solving?]

### Options Considered
[List alternatives with pros/cons]

### Decision Made
[What you chose]

### Reasoning
[Why you chose it - technical, UX, business reasons]

### Interview Talking Point
[How to explain this decision in 2-3 sentences]

---

## Meta: Why This Document Exists

This log demonstrates:
- **Critical Thinking:** Not just implementing features, but questioning requirements
- **Tradeoff Analysis:** Understanding pros/cons of different approaches
- **User-Centered Design:** Considering actual user needs, not just technical capabilities
- **Iterative Development:** Build simple first, add complexity when justified
- **Production Awareness:** Knowing difference between MVP and production-ready

**For Interviews:** Shows you think like an engineer who makes intentional decisions, not just someone who follows tutorials.