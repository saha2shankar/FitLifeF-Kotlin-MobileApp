package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.viewmodel.ExerciseViewModel

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ExerciseScreen(
    onCreateExerciseClick: () -> Unit,
    viewModel: ExerciseViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    // Register GlobalDialog for confirmation popups
    GlobalDialog()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

        ) {
            Text(
                text = "Exercise",
                style = MaterialTheme.typography.headlineLarge,
                color = OnPrimary
            )

            GlobalIconButton(
                icon = Icons.Filled.Add,
                contentDescription = "Add Workout",
                onClick = {
                    onCreateExerciseClick()
                }

            )
        }
        Spacer(modifier = Modifier.height(Size.sm))
        GlobalTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = "Search",
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.sm))


        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
            }
        } else {
            if (state.exercises.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = "No exercises",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No exercises found",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to create your first exercise",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
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
                            if (it == DismissValue.DismissedToStart) {
                                // Swipe Left -> Delete with Confirmation
                                GlobalDialogState.showConfirmation(
                                    title = "Delete Exercise",
                                    message = "Are you sure you want to delete '${exercise.routineName}'?",
                                    onConfirm = {
                                        viewModel.deleteExercise(exercise.id)
                                    }
                                )
                                false // Return false to snap back, logic handled in dialog
                            } else if (it == DismissValue.DismissedToEnd) {
                                // Swipe Right -> Edit
                                val intent = Intent(context, CreateExerciseActivity::class.java).apply {
                                    putExtra("exercise_data", Gson().toJson(exercise))
                                }
                                context.startActivity(intent)
                                false // Return false to snap back
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                        background = {
                            val color = when (dismissState.dismissDirection) {
                                DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                                DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                                null -> Color.Transparent
                            }
                            val alignment = when (dismissState.dismissDirection) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                                null -> Alignment.Center
                            }
                            val icon = when (dismissState.dismissDirection) {
                                DismissDirection.StartToEnd -> Icons.Default.Edit
                                DismissDirection.EndToStart -> Icons.Default.Delete
                                null -> Icons.Default.Delete
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "Action",
                                    tint = Color.White
                                )
                            }
                        },
                        dismissContent = {
                            ExerciseItem(
                                exercise = exercise, 
                                onDelete = { 
                                    GlobalDialogState.showConfirmation(
                                        title = "Delete Exercise",
                                        message = "Are you sure you want to delete '${exercise.routineName}'?",
                                        onConfirm = {
                                            viewModel.deleteExercise(exercise.id)
                                        }
                                    )
                                },
                                onEdit = {
                                    val intent = Intent(context, CreateExerciseActivity::class.java).apply {
                                        putExtra("exercise_data", Gson().toJson(exercise))
                                    }
                                    context.startActivity(intent)
                                },
                                onClick = {
                                    val intent = Intent(context, ExerciseDetailActivity::class.java).apply {
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

@Composable
fun ExerciseItem(exercise: ExerciseModel, onDelete: () -> Unit, onEdit: () -> Unit, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            if (!exercise.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)

                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.routineName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.ModeEdit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = exercise.instructions,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )
        }
    }
    }
}
