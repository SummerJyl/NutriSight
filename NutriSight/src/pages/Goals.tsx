import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

interface NutritionGoal {
  id?: number;
  userId: number;
  goalType: 'DAILY' | 'WEEKLY';
  targetCalories: number;
  targetProtein: number;
  targetCarbs: number;
  targetFat: number;
  startDate?: string;
  endDate?: string | null;
  isActive?: boolean;
}

interface UserData {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
}

const Goals = () => {
  const [user, setUser] = useState<UserData | null>(null);
  const [currentGoal, setCurrentGoal] = useState<NutritionGoal | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Form state
  const [goalType, setGoalType] = useState<'DAILY' | 'WEEKLY'>('DAILY');
  const [targetCalories, setTargetCalories] = useState(2000);
  const [targetProtein, setTargetProtein] = useState(150);
  const [targetCarbs, setTargetCarbs] = useState(200);
  const [targetFat, setTargetFat] = useState(65);

  useEffect(() => {
    // Check if user is logged in
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      navigate('/login');
      return;
    }

    const userData = JSON.parse(storedUser);
    setUser(userData);

    // Fetch active goal
    fetchActiveGoal(userData.userId);
  }, [navigate]);

  const fetchActiveGoal = async (userId: number) => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/goals/user/${userId}/active`);
      
      if (response.ok) {
        const goal = await response.json();
        setCurrentGoal(goal);
        // Populate form with current values
        setGoalType(goal.goalType);
        setTargetCalories(goal.targetCalories);
        setTargetProtein(goal.targetProtein);
        setTargetCarbs(goal.targetCarbs);
        setTargetFat(goal.targetFat);
      } else if (response.status === 404) {
        // No active goal found - that's okay, user can create one
        setCurrentGoal(null);
        setIsEditing(true);
      }
    } catch (err) {
      setError('Failed to load goals. Make sure backend is running.');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;

    setLoading(true);
    setError('');

    const goalData: NutritionGoal = {
      userId: user.userId,
      goalType,
      targetCalories,
      targetProtein,
      targetCarbs,
      targetFat,
    };

    try {
      let response;
      
      if (currentGoal?.id) {
        // Update existing goal
        response = await fetch(`http://localhost:8080/api/goals/${currentGoal.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(goalData),
        });
      } else {
        // Create new goal
        response = await fetch('http://localhost:8080/api/goals', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(goalData),
        });
      }

      if (response.ok) {
        const updatedGoal = await response.json();
        setCurrentGoal(updatedGoal);
        setIsEditing(false);
      } else {
        setError('Failed to save goal');
      }
    } catch (err) {
      setError('Network error. Make sure backend is running.');
    } finally {
      setLoading(false);
    }
  };

  if (loading && !currentGoal) {
    return <div style={{ padding: '2rem' }}>Loading...</div>;
  }

  return (
    <div style={{ maxWidth: '800px', margin: '2rem auto', padding: '2rem' }}>
      <button
        onClick={() => navigate('/')}
        style={{
          marginBottom: '1rem',
          padding: '0.5rem 1rem',
          backgroundColor: '#6c757d',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer',
        }}
      >
        ← Back to Home
      </button>

      <h1>My Nutrition Goals</h1>

      {error && (
        <div style={{ padding: '1rem', backgroundColor: '#f8d7da', color: '#721c24', borderRadius: '4px', marginBottom: '1rem' }}>
          {error}
        </div>
      )}

      {!isEditing && currentGoal ? (
        // Display current goal
        <div style={{ backgroundColor: '#f8f9fa', padding: '2rem', borderRadius: '8px', marginBottom: '2rem' }}>
          <h2>Current Active Goal</h2>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginTop: '1rem' }}>
            <div>
              <strong>Goal Type:</strong> {currentGoal.goalType}
            </div>
            <div>
              <strong>Calories:</strong> {currentGoal.targetCalories} kcal
            </div>
            <div>
              <strong>Protein:</strong> {currentGoal.targetProtein}g
            </div>
            <div>
              <strong>Carbs:</strong> {currentGoal.targetCarbs}g
            </div>
            <div>
              <strong>Fat:</strong> {currentGoal.targetFat}g
            </div>
            <div>
              <strong>Start Date:</strong> {currentGoal.startDate}
            </div>
          </div>
          <button
            onClick={() => setIsEditing(true)}
            style={{
              marginTop: '1.5rem',
              padding: '0.75rem 1.5rem',
              backgroundColor: '#007bff',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontSize: '1rem',
            }}
          >
            Edit Goal
          </button>
        </div>
      ) : (
        // Edit/Create form
        <form onSubmit={handleSubmit} style={{ backgroundColor: '#fff', padding: '2rem', borderRadius: '8px', border: '1px solid #dee2e6' }}>
          <h2>{currentGoal ? 'Edit Goal' : 'Create New Goal'}</h2>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              Goal Type:
            </label>
            <select
              value={goalType}
              onChange={(e) => setGoalType(e.target.value as 'DAILY' | 'WEEKLY')}
              style={{ width: '100%', padding: '0.5rem', fontSize: '1rem', borderRadius: '4px', border: '1px solid #ced4da' }}
            >
              <option value="DAILY">Daily</option>
              <option value="WEEKLY">Weekly</option>
            </select>
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              Target Calories (kcal):
            </label>
            <input
              type="number"
              value={targetCalories}
              onChange={(e) => setTargetCalories(Number(e.target.value))}
              min="0"
              style={{ width: '100%', padding: '0.5rem', fontSize: '1rem', borderRadius: '4px', border: '1px solid #ced4da' }}
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              Target Protein (g):
            </label>
            <input
              type="number"
              value={targetProtein}
              onChange={(e) => setTargetProtein(Number(e.target.value))}
              min="0"
              style={{ width: '100%', padding: '0.5rem', fontSize: '1rem', borderRadius: '4px', border: '1px solid #ced4da' }}
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              Target Carbs (g):
            </label>
            <input
              type="number"
              value={targetCarbs}
              onChange={(e) => setTargetCarbs(Number(e.target.value))}
              min="0"
              style={{ width: '100%', padding: '0.5rem', fontSize: '1rem', borderRadius: '4px', border: '1px solid #ced4da' }}
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              Target Fat (g):
            </label>
            <input
              type="number"
              value={targetFat}
              onChange={(e) => setTargetFat(Number(e.target.value))}
              min="0"
              style={{ width: '100%', padding: '0.5rem', fontSize: '1rem', borderRadius: '4px', border: '1px solid #ced4da' }}
            />
          </div>

          <div style={{ display: 'flex', gap: '1rem' }}>
            <button
              type="submit"
              disabled={loading}
              style={{
                padding: '0.75rem 1.5rem',
                backgroundColor: '#28a745',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: loading ? 'not-allowed' : 'pointer',
                fontSize: '1rem',
              }}
            >
              {loading ? 'Saving...' : 'Save Goal'}
            </button>
            
            {currentGoal && (
              <button
                type="button"
                onClick={() => setIsEditing(false)}
                style={{
                  padding: '0.75rem 1.5rem',
                  backgroundColor: '#6c757d',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '1rem',
                }}
              >
                Cancel
              </button>
            )}
          </div>
        </form>
      )}
    </div>
  );
};

export default Goals;