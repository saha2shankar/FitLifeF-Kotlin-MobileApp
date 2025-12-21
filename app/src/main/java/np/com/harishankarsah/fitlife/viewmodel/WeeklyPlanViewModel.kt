package np.com.harishankarsah.fitlife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel
import np.com.harishankarsah.fitlife.repository.WeeklyPlanRepository

data class WeeklyPlanState(
    val plans: List<WeeklyPlanModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class WeeklyPlanViewModel : ViewModel() {
    private val repository = WeeklyPlanRepository()

    private val _state = MutableStateFlow(WeeklyPlanState())
    val state: StateFlow<WeeklyPlanState> = _state.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")

    init {
        // Collect real-time updates and filter based on search query
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            combine(
                repository.getWeeklyPlansFlow(),
                _searchQuery
            ) { plans, query ->
                if (query.isBlank()) {
                    plans
                } else {
                    plans.filter {
                        it.exerciseName.contains(query, ignoreCase = true) ||
                        it.day.contains(query, ignoreCase = true)
                    }
                }
            }
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .collect { filteredPlans ->
                    _state.value = _state.value.copy(plans = filteredPlans, isLoading = false, error = null)
                }
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _state.value = _state.value.copy(searchQuery = query)
    }

    // Kept for manual refresh if needed, but flow handles updates
    fun loadWeeklyPlans() {
        // No-op as flow is active
    }

    fun toggleCompletion(plan: WeeklyPlanModel) {
        viewModelScope.launch {
            val updatedPlan = plan.copy(completed = !plan.completed)
            repository.updateWeeklyPlan(updatedPlan)
                .onFailure { e ->
                    _state.value = _state.value.copy(error = e.message)
                }
        }
    }
    
    fun markPlanCompleted(plan: WeeklyPlanModel) {
        if (plan.completed) return // Already completed
        
        viewModelScope.launch {
            val updatedPlan = plan.copy(completed = true)
            repository.updateWeeklyPlan(updatedPlan)
                .onFailure { e ->
                    _state.value = _state.value.copy(error = e.message)
                }
        }
    }

    fun deletePlan(planId: String) {
        viewModelScope.launch {
            repository.deleteWeeklyPlan(planId)
                .onFailure { e ->
                    _state.value = _state.value.copy(error = e.message)
                }
        }
    }
}
