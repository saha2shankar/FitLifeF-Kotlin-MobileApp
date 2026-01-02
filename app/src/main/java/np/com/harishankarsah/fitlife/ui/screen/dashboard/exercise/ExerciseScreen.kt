package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.gson.Gson
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.theme.Error
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ExerciseScreen(
    onCreateExerciseClick: () -> Unit,
    viewModel: ExerciseViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    GlobalDialog()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Scaffold(
        containerColor = OnAccent,
                floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateExerciseClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Exercise",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
           modifier =  Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Workout",
                style = MaterialTheme.typography.headlineLarge,
                color = OnPrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(Size.sm))

            // Search
            GlobalTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = "Search",
                modifier = Modifier.padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            )

            Spacer(modifier = Modifier.height(Size.sm))

            // Content
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
                            text = state.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                state.exercises.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No exercises found",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap + to add your first exercise",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = state.exercises,
                            key = { it.id }
                        ) { exercise ->

                            val dismissState = rememberDismissState(
                                confirmStateChange = {
                                    when (it) {
                                        DismissValue.DismissedToStart -> {
                                            GlobalDialogState.showConfirmation(
                                                title = "Delete Exercise",
                                                message = "Delete '${exercise.routineName}'?",
                                                onConfirm = {
                                                    viewModel.deleteExercise(exercise.id)
                                                }
                                            )
                                            false
                                        }

                                        DismissValue.DismissedToEnd -> {
                                            val intent = Intent(
                                                context,
                                                CreateExerciseActivity::class.java
                                            ).apply {
                                                putExtra(
                                                    "exercise_data",
                                                    Gson().toJson(exercise)
                                                )
                                            }
                                            context.startActivity(intent)
                                            false
                                        }

                                        else -> false
                                    }
                                }
                            )

                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.StartToEnd,
                                    DismissDirection.EndToStart
                                ),
                                background = {
                                    val color = when (dismissState.dismissDirection) {
                                        DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                                        DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                                        else -> Color.Transparent
                                    }

                                    val icon = when (dismissState.dismissDirection) {
                                        DismissDirection.StartToEnd -> Icons.Default.Edit
                                        DismissDirection.EndToStart -> Icons.Default.Delete
                                        else -> Icons.Default.Delete
                                    }

                                    val alignment = when (dismissState.dismissDirection) {
                                        DismissDirection.StartToEnd -> Alignment.CenterStart
                                        DismissDirection.EndToStart -> Alignment.CenterEnd
                                        else -> Alignment.Center
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = alignment
                                    ) {
                                        Icon(icon, null, tint = Color.White)
                                    }
                                },
                                dismissContent = {
                                    ExerciseItem(
                                        exercise = exercise,
                                        onDelete = {
                                            GlobalDialogState.showConfirmation(
                                                title = "Delete Exercise",
                                                message = "Delete '${exercise.routineName}'?",
                                                onConfirm = {
                                                    viewModel.deleteExercise(exercise.id)
                                                }
                                            )
                                        },
                                        onEdit = {
                                            val intent = Intent(
                                                context,
                                                CreateExerciseActivity::class.java
                                            ).apply {
                                                putExtra(
                                                    "exercise_data",
                                                    Gson().toJson(exercise)
                                                )
                                            }
                                            context.startActivity(intent)
                                        },
                                        onClick = {
                                            val intent = Intent(
                                                context,
                                                ExerciseDetailActivity::class.java
                                            ).apply {
                                                putExtra("exercise_id", exercise.id)
                                            }
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseModel,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if (!exercise.imageUrl.isNullOrEmpty()) {
                Spacer(modifier = Modifier.width(10.dp))
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = exercise.routineName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, null, tint = Primary)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, null, tint = Error)
                    }
                }

                Text(
                    text = exercise.instructions
                        .replace(Regex("\\s+"), " ")
                        .trim(),
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
