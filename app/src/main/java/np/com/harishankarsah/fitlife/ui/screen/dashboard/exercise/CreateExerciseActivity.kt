package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import np.com.harishankarsah.fitlife.ui.components.location.FullScreenLocationPickerScreen
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import np.com.harishankarsah.fitlife.viewmodel.CreateExerciseViewModel
import np.com.harishankarsah.fitlife.model.ExerciseModel
import com.google.gson.Gson

class CreateExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val exerciseJson = intent.getStringExtra("exercise_data")
        val exerciseToEdit = if (exerciseJson != null) {
             Gson().fromJson(exerciseJson, ExerciseModel::class.java)
        } else null

        setContent {
            FitLifeTheme {

                val navController = rememberNavController()
                
                // Initialize ViewModel here to survive navigation
                val viewModel: CreateExerciseViewModel = viewModel()
                
                // Initialize if editing (only once)
                LaunchedEffect(Unit) {
                    if (exerciseToEdit != null && !viewModel.isEditMode) {
                        viewModel.initializeForEdit(exerciseToEdit)
                    }
                }

                var selectedLocation by remember {
                    mutableStateOf<LatLng?>(
                        if (exerciseToEdit != null && exerciseToEdit.latitude != null && exerciseToEdit.longitude != null) {
                            LatLng(exerciseToEdit.latitude, exerciseToEdit.longitude)
                        } else null
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = "create_exercise"
                ) {

                    composable("create_exercise") {
                        CreateExerciseScreen(
                            viewModel = viewModel,
                            selectedLocation = selectedLocation,
                            onPickLocation = {
                                navController.navigate("location_picker")
                            },
                            onBackClick = {
                                finish() // ✅ correct back behavior
                            }
                        )
                    }

                    composable("location_picker") {
                        FullScreenLocationPickerScreen(
                            initialLocation = selectedLocation
                                ?: LatLng(27.7172, 85.3240),
                            onBack = {
                                navController.popBackStack()
                            },
                            onConfirm = {
                                selectedLocation = it
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
