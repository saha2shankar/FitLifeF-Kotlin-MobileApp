package np.com.harishankarsah.fitlife.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.repository.ExerciseRepository
import np.com.harishankarsah.fitlife.repository.WeeklyPlanRepository
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel

data class ExerciseDetailState(
    val exercise: ExerciseModel? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDeleted: Boolean = false,
    val isPlanAdded: Boolean = false,
    val planMessage: String? = null
)

class ExerciseDetailViewModel : ViewModel() {
    private val repository = ExerciseRepository()
    private val weeklyPlanRepository = WeeklyPlanRepository()
    
    var state by mutableStateOf(ExerciseDetailState())
        private set

    fun addToWeeklyPlan(day: String, reps: String, sets: String, notes: String) {
        val currentExercise = state.exercise ?: return
        
        val repsInt = reps.toIntOrNull() ?: 0
        val setsInt = sets.toIntOrNull() ?: 0

        viewModelScope.launch {
            val plan = WeeklyPlanModel(
                exerciseId = currentExercise.id,
                exerciseName = currentExercise.routineName,
                imageUrl = currentExercise.imageUrl,
                day = day,
                reps = repsInt,
                sets = setsInt,
                notes = notes
            )
            weeklyPlanRepository.addWeeklyPlan(plan)
                .onSuccess {
                    state = state.copy(isPlanAdded = true, planMessage = "Added to Weekly Plan")
                }
                .onFailure { e ->
                    state = state.copy(isPlanAdded = false, planMessage = "Failed: ${e.message}")
                }
        }
    }

    fun resetPlanState() {
        state = state.copy(isPlanAdded = false, planMessage = null)
    }

    fun loadExercise(exerciseId: String) {
        viewModelScope.launch {
            repository.getExerciseFlow(exerciseId).collectLatest { exercise ->
                // If exercise is null, it means it doesn't exist (deleted or invalid ID)
                // If we were viewing it, it implies it was deleted.
                if (exercise == null) {
                     state = state.copy(isLoading = false, exercise = null, isDeleted = true)
                } else {
                     state = state.copy(isLoading = false, exercise = exercise, isDeleted = false)
                }
            }
        }
    }

    fun deleteExercise(exerciseId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteExercise(exerciseId)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { e ->
                    state = state.copy(error = e.message)
                }
        }
    }
}
