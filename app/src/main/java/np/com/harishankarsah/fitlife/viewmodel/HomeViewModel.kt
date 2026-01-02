package np.com.harishankarsah.fitlife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.repository.ExerciseRepository
import np.com.harishankarsah.fitlife.repository.WeeklyPlanRepository

data class HomeState(
    val totalWeeklyPlans: Int = 0,
    val completedWeeklyPlans: Int = 0,
    val totalExercises: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val weeklyPlanRepository = WeeklyPlanRepository()
    private val exerciseRepository = ExerciseRepository()
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    init {
        // Collect real-time updates for all dashboard stats
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            combine(
                weeklyPlanRepository.getWeeklyPlansCountFlow(),
                weeklyPlanRepository.getCompletedWeeklyPlansCountFlow(),
                exerciseRepository.getExercisesCountFlow()
            ) { totalPlans, completedPlans, totalExercises ->
                Triple(totalPlans, completedPlans, totalExercises)
            }
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .collect { (totalPlans, completedPlans, totalExercises) ->
                    _state.value = _state.value.copy(
                        totalWeeklyPlans = totalPlans,
                        completedWeeklyPlans = completedPlans,
                        totalExercises = totalExercises,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}




