package np.com.harishankarsah.fitlife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.repository.ExerciseRepository

data class ExerciseState(
    val isLoading: Boolean = false,
    val exercises: List<ExerciseModel> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val searchQuery: String = ""
)

class ExerciseViewModel : ViewModel() {
    private val repository = ExerciseRepository()
    private val _state = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        // Seed default exercises if needed
        viewModelScope.launch {
            repository.seedDefaultExercises()
        }

        // Collect real-time updates and filter based on search query
        viewModelScope.launch {
            combine(
                repository.getExercisesFlow(),
                _searchQuery
            ) { exercises, query ->
                if (query.isBlank()) {
                    exercises
                } else {
                    exercises.filter {
                        it.routineName.contains(query, ignoreCase = true) ||
                        it.instructions.contains(query, ignoreCase = true)
                    }
                }
            }
            .catch { e ->
                _state.value = _state.value.copy(error = e.message)
            }
            .collect { filteredExercises ->
                _state.value = _state.value.copy(
                    exercises = filteredExercises,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _state.value = _state.value.copy(searchQuery = query)
    }

    // loadExercises is no longer needed to be called manually, 
    // but we can keep it empty or remove usages if we refactor consumers.
    // For now, let's keep it but make it do nothing as init block handles it.
    fun loadExercises() {
        // No-op: Real-time updates are active
    }

    fun createExercise(exercise: ExerciseModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.addExercise(exercise)
            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false, successMessage = "Exercise created successfully")
                // loadExercises() - Not needed, flow updates automatically
                onSuccess()
            }.onFailure { exception ->
                _state.value = _state.value.copy(isLoading = false, error = exception.message)
            }
        }
    }

    fun deleteExercise(exerciseId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.deleteExercise(exerciseId)
            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false, successMessage = "Exercise deleted successfully")
                // loadExercises() - Not needed, flow updates automatically
            }.onFailure { exception ->
                _state.value = _state.value.copy(isLoading = false, error = exception.message)
            }
        }
    }

    fun updateExercise(exercise: ExerciseModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.updateExercise(exercise)
            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false, successMessage = "Exercise updated successfully")
                // loadExercises() - Not needed, flow updates automatically
                onSuccess()
            }.onFailure { exception ->
                _state.value = _state.value.copy(isLoading = false, error = exception.message)
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}
