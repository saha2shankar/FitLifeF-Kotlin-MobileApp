package np.com.harishankarsah.fitlife.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.repository.ExerciseRepository

data class CreateExerciseState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class CreateExerciseViewModel : ViewModel() {
    private val repository = ExerciseRepository()

    var state by mutableStateOf(CreateExerciseState())
        private set

    // Form State
    var selectedImage by mutableStateOf<Uri?>(null)
    var routineName by mutableStateOf("")
    var instructions by mutableStateOf("")
    var exercises by mutableStateOf(listOf<String>())
    var equipment by mutableStateOf(listOf<String>())

    // Edit Mode State
    var isEditMode by mutableStateOf(false)
        private set
    private var currentExerciseId: String? = null

    fun initializeForEdit(exercise: ExerciseModel) {
        isEditMode = true
        currentExerciseId = exercise.id
        routineName = exercise.routineName
        instructions = exercise.instructions
        exercises = exercise.exercises
        equipment = exercise.equipment
        selectedImage = exercise.imageUrl?.let { Uri.parse(it) }
    }

    fun clearMessages() {
        state = state.copy(error = null, successMessage = null)
    }

    fun saveExercise(
        localImagePath: String?,
        latitude: Double?,
        longitude: Double?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, successMessage = null)
            
            val exerciseData = ExerciseModel(
                id = currentExerciseId ?: "",
                routineName = routineName,
                instructions = instructions,
                exercises = exercises,
                equipment = equipment,
                imageUrl = localImagePath,
                latitude = latitude,
                longitude = longitude
            )

            val result = if (isEditMode) {
                repository.updateExercise(exerciseData)
            } else {
                repository.addExercise(exerciseData)
            }

            result.onSuccess {
                state = state.copy(
                    isLoading = false,
                    successMessage = if (isEditMode) "Exercise updated successfully" else "Exercise created successfully"
                )
                onSuccess()
            }.onFailure { exception ->
                state = state.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error"
                )
            }
        }
    }
}
