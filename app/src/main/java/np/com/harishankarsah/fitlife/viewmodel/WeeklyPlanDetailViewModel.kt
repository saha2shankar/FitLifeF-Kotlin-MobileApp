package np.com.harishankarsah.fitlife.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel
import np.com.harishankarsah.fitlife.repository.ExerciseRepository
import np.com.harishankarsah.fitlife.repository.WeeklyPlanRepository

data class WeeklyPlanDetailState(
    val plan: WeeklyPlanModel? = null,
    val exercise: ExerciseModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDeleted: Boolean = false
)

class WeeklyPlanDetailViewModel : ViewModel() {
    private val repository = WeeklyPlanRepository()
    private val exerciseRepository = ExerciseRepository()

    var state by mutableStateOf(WeeklyPlanDetailState())
        private set

    fun loadPlan(planId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.getWeeklyPlanById(planId)
                .onSuccess { plan ->
                    state = state.copy(plan = plan, error = null)
                    // Fetch exercise details
                    if (plan.exerciseId.isNotEmpty()) {
                        exerciseRepository.getExerciseById(plan.exerciseId)
                            .onSuccess { exercise ->
                                state = state.copy(exercise = exercise, isLoading = false)
                            }
                            .onFailure { e ->
                                // Keep plan but show error? Or just log? 
                                // For now, just stop loading, maybe exercise was deleted.
                                state = state.copy(isLoading = false)
                            }
                    } else {
                        state = state.copy(isLoading = false)
                    }
                }
                .onFailure { e ->
                    state = state.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun toggleCompletion() {
        val currentPlan = state.plan ?: return
        viewModelScope.launch {
            val updatedPlan = currentPlan.copy(completed = !currentPlan.completed)
            repository.updateWeeklyPlan(updatedPlan)
                .onSuccess {
                    state = state.copy(plan = updatedPlan)
                }
                .onFailure { e ->
                    state = state.copy(error = e.message)
                }
        }
    }

    fun deletePlan(onSuccess: () -> Unit) {
        val planId = state.plan?.id ?: return
        viewModelScope.launch {
            repository.deleteWeeklyPlan(planId)
                .onSuccess {
                    state = state.copy(isDeleted = true)
                    onSuccess()
                }
                .onFailure { e ->
                    state = state.copy(error = e.message)
                }
        }
    }
}
