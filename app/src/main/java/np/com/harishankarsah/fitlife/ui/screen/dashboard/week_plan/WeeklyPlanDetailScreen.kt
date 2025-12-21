package np.com.harishankarsah.fitlife.ui.screen.dashboard.week_plan

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.RectangleShape
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.IconButtonType
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.components.location.GlobalMapView
import np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise.SectionTitle
import np.com.harishankarsah.fitlife.ui.theme.Info
import np.com.harishankarsah.fitlife.ui.theme.OnSecondary
import np.com.harishankarsah.fitlife.ui.theme.OnSurface
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.viewmodel.WeeklyPlanDetailViewModel
import np.com.harishankarsah.fitlife.ui.theme.Success
import np.com.harishankarsah.fitlife.ui.theme.Surface
import np.com.harishankarsah.fitlife.ui.utils.Size

@Composable
fun WeeklyPlanDetailScreen(
    planId: String,
    onBack: () -> Unit,
    viewModel: WeeklyPlanDetailViewModel = viewModel()
) {
    val state = viewModel.state

    // Register Global Dialog
    GlobalDialog()

    LaunchedEffect(planId) {
        viewModel.loadPlan(planId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GlobalIconButton(
            icon = Icons.Default.ArrowBackIos,
            contentDescription = "Back",
            onClick = {
                onBack()
            },
            buttonType = IconButtonType.PRIMARY
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding( vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Plan Details",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (state.plan != null) {
                IconButton(onClick = {
                    GlobalDialogState.showConfirmation(
                        title = "Delete Plan",
                        message = "Are you sure you want to delete this plan?",
                        onConfirm = {
                            viewModel.deletePlan(onSuccess = onBack)
                        }
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

        }

        /* ---------- CONTENT ---------- */
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.plan != null -> {
                val plan = state.plan!!
                val exercise = state.exercise

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    /* ---------- IMAGE ---------- */
                    if (!exercise?.imageUrl.isNullOrEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            AsyncImage(
                                model = File(exercise!!.imageUrl),
                                contentDescription = "Exercise Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    /* ---------- HEADER CARD ---------- */
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = plan.day,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = plan.exerciseName,
                                style = MaterialTheme.typography.headlineMedium,
                                color = OnSecondary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = if (plan.completed)
                                    TextDecoration.LineThrough else TextDecoration.None
                            )

                            exercise?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Routine: ${it.routineName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Info
                                )
                            }
                        }
                    }

                    /* ---------- PLAN DETAILS ---------- */
                    Card(modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                            containerColor = Surface
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Plan Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            RowDetail("Sets", plan.sets.toString())
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            RowDetail("Reps", plan.reps.toString())

                            if (plan.notes.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "Notes",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(plan.notes)
                            }
                        }
                    }

                    /* ---------- EXERCISE DETAILS ---------- */
                    if (exercise != null) {
                        Card(modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Surface
                            )) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Exercise Instructions",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(exercise.instructions)
                                if (exercise.exercises.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Sub-Exercises",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Info
                                    )
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


                                }
                                if (exercise.equipment.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Equipment Needed",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Info

                                    )
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
                                                        Icons.Default.Build,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            )
                                        }
                                    }

                                }
                                if (exercise.latitude != null && exercise.longitude != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Selected Location",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Info

                                    )

                                    Text(
                                        text = "Lat: ${exercise.latitude}, Lng: ${exercise.longitude}",
                                        style = MaterialTheme.typography.bodyMedium
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
                            }
                        }
                    }

                    /* ---------- ACTION ---------- */

                    if (plan.completed) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, tint = Success, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Completed", color = Success, fontWeight = FontWeight.Bold)
                        }
                    }

                    GlobalButton(
                        text = if (plan.completed)
                            "Mark as Incomplete" else "Mark as Completed",
                        onClick = { viewModel.toggleCompletion() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

/* ---------- HELPER ---------- */
@Composable
fun RowDetail(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}
