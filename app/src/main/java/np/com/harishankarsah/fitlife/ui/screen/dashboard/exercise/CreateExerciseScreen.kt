package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.viewmodel.CreateExerciseViewModel

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import np.com.harishankarsah.fitlife.ui.components.*
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.components.location.GlobalLocationPickerCard
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import np.com.harishankarsah.fitlife.utils.saveImageToInternalStorage

@Composable
fun CreateExerciseScreen(
    selectedLocation: LatLng?,
    onPickLocation: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreateExerciseViewModel = viewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    // Register GlobalDialog
    GlobalDialog()

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            GlobalDialogState.showSuccess(msg = it)
            viewModel.clearMessages()
            onBackClick()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            GlobalDialogState.showError(msg = it)
            viewModel.clearMessages()
        }
    }

    Column {
        Spacer(modifier = Modifier.height(50.dp))

        GlobalIconButton(
            icon = Icons.Default.ArrowBackIos,
            contentDescription = "Back",
            onClick = onBackClick,
            buttonType = IconButtonType.PRIMARY
        )

        Spacer(modifier = Modifier.height(Size.lg))

        Text(
            text = if (viewModel.isEditMode) "Edit Workout" else "Create Workout",
            style = MaterialTheme.typography.headlineLarge,
            color = OnPrimary
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            GlobalTextField(
                value = viewModel.routineName,
                isRequired = true,
                onValueChange = { viewModel.routineName = it },
                label = "Routine Name",
                placeholder = "Enter routine name"
            )

            Spacer(modifier = Modifier.height(Size.sm))

            GlobalTextField(
                value = viewModel.instructions,
                isRequired = true,
                onValueChange = { viewModel.instructions = it },
                label = "Instructions",
                placeholder = "Enter instructions"
            )

            Spacer(modifier = Modifier.height(Size.sm))

            Spacer(modifier = Modifier.height(Size.sm))

            GlobalMultiInputField(
                title = "Exercises",
                items = viewModel.exercises,
                onItemsChange = { viewModel.exercises = it },
                placeholder = "Add exercise (e.g. Push Ups)"
            )

            Spacer(modifier = Modifier.height(Size.sm))

            GlobalMultiInputField(
                title = "Equipment",
                items = viewModel.equipment,
                onItemsChange = { viewModel.equipment = it },
                placeholder = "Add equipment (e.g. Dumbbell)"
            )


            Spacer(modifier = Modifier.height(Size.sm))

            // ✅ FULL SCREEN LOCATION PICKER ENTRY
            GlobalLocationPickerCard(
                selectedLocation = selectedLocation,
                onClick = onPickLocation
            )

            Spacer(modifier = Modifier.height(Size.sm))

            GlobalImagePicker(
                modifier = Modifier.fillMaxWidth(),
                imageUri = viewModel.selectedImage,
                onImagePicked = { viewModel.selectedImage = it },
                title = "Workout Image",
                height = 140.dp
            )

            Spacer(modifier = Modifier.height(Size.lg))

            GlobalButton(
                text = if (viewModel.isEditMode) "Update Exercise" else "Create Exercise",
                onClick = {
                    if (viewModel.routineName.isNotBlank() && viewModel.instructions.isNotBlank()) {
                        scope.launch(Dispatchers.IO) {
                            val localImagePath = viewModel.selectedImage?.let { uri ->
                                // If the URI is already a local file path (string), we don't need to save it again.
                                // However, selectedImage is Uri.
                                // If it's a content:// URI, save it.
                                // If it's a file:// URI or just a path parsed as URI, we might check scheme.
                                if (uri.scheme == "content") {
                                    saveImageToInternalStorage(context, uri)
                                } else {
                                    uri.toString() // Use existing path
                                }
                            }
                            
                            withContext(Dispatchers.Main) {
                                viewModel.saveExercise(
                                    localImagePath = localImagePath,
                                    latitude = selectedLocation?.latitude,
                                    longitude = selectedLocation?.longitude
                                ) {
                                    // Success callback
                                }
                            }
                        }
                    } else {
                        GlobalDialogState.showError(msg = "Please fill required fields")
                    }
                },
                buttonType = ButtonType.PRIMARY
            )
            Spacer(modifier = Modifier.height(100.dp))

        }
    }
}
