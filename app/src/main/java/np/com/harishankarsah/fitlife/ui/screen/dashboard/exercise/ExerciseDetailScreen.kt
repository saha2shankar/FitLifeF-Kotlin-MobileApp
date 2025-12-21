package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.gson.Gson
import np.com.harishankarsah.fitlife.ui.components.ButtonType
import np.com.harishankarsah.fitlife.ui.components.EquipmentDelegationCardSMS
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.components.dialog.DialogField
import np.com.harishankarsah.fitlife.ui.components.dialog.DialogType
import np.com.harishankarsah.fitlife.ui.components.dialog.FieldType
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalMultiFieldDialog
import np.com.harishankarsah.fitlife.ui.components.location.GlobalMapView
import np.com.harishankarsah.fitlife.ui.theme.Error
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnBackground
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.viewmodel.ExerciseDetailViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ExerciseDetailViewModel = viewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    // Register Dialog Components
    GlobalDialog()
    GlobalMultiFieldDialog()

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }

    // Handle deletion (either by user or remote)
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onBack()
        }
    }

    // Handle Weekly Plan addition result
    LaunchedEffect(state.planMessage) {
        if (state.planMessage != null) {
            if (state.isPlanAdded) {
                GlobalDialogState.showSuccess(msg = state.planMessage)
            } else {
                GlobalDialogState.showError(msg = state.planMessage)
            }
            viewModel.resetPlanState()
        }
    }

    Scaffold(
        containerColor = OnAccent,
        topBar = {
            TopAppBar(
                title = { Text("Exercise Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.exercise != null) {
                        IconButton(onClick = {
                            val intent = Intent(context, CreateExerciseActivity::class.java).apply {
                                putExtra("exercise_data", Gson().toJson(state.exercise))
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Primary)
                        }
                        IconButton(onClick = {
                            viewModel.deleteExercise(exerciseId) {
                                onBack()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.exercise != null) {
            val exercise = state.exercise!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Image
                if (exercise.imageUrl != null) {
                    AsyncImage(
                        model = File(exercise.imageUrl), // Assuming local path
                        contentDescription = "Exercise Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    // Routine Name
                    Text(
                        text = exercise.routineName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground

                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Instructions
                    SectionTitle("Instructions")
                    Text(text = exercise.instructions, style = MaterialTheme.typography.bodyLarge,
                        color = OnBackground)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Exercises List
                    if (exercise.exercises.isNotEmpty()) {
                        SectionTitle("Exercises")
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            exercise.exercises.forEach { item ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(item) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.FitnessCenter,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Equipment List
                    if (exercise.equipment.isNotEmpty()) {
                        SectionTitle("Equipment")

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            exercise.equipment.forEach { item ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(item) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.BuildCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        // Equipment Delegation Card
                        EquipmentDelegationCardSMS(
                            equipmentList = exercise.equipment
                        )
                    }


                    // Location
                    if (exercise.latitude != null && exercise.longitude != null) {
                        SectionTitle("Location")

                        Text(
                            text = "Lat: ${exercise.latitude}, Lng: ${exercise.longitude}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnBackground
                        )
                        Spacer(modifier = Modifier.height(Size.sm))
                        GlobalMapView(
                            latitude = exercise.latitude!!,   // use actual exercise coordinates
                            longitude = exercise.longitude!!, // use actual exercise coordinates
                            markerTitle = "Workout Location" ,
                            cardHeight = 250.dp,
                            elevation = 10.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                    }


                    Row( modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        GlobalButton(
                            text = "Add to Plan",
                            onClick = {
                                GlobalDialogState.showMultiField(
                                    title = "Add to Weekly Plan",
                                    fields = listOf(
                                        DialogField("Day", "Select your day", FieldType.Dropdown(listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"))),
                                        DialogField("Reps", "Enter reps (e.g., 12, 15)", FieldType.Number),
                                        DialogField("Sets", "Enter sets (e.g., 3)", FieldType.Number),
                                        DialogField("Additional Notes", "Additional notes (optional)", FieldType.Text),

                                        ),
                                    onConfirm = { result ->
                                        val day = result["Day"] as? String ?: ""
                                        val reps = result["Reps"] as? String ?: ""
                                        val sets = result["Sets"] as? String ?: ""
                                        val notes = result["Additional Notes"] as? String ?: ""
                                        viewModel.addToWeeklyPlan(day, reps, sets, notes)
                                    }
                                )
                            },
                            buttonType = ButtonType.OUTLINED
                        )
                    }

                }
            }
        } else if (state.error != null) {
             Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
             Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Exercise not found")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
}
