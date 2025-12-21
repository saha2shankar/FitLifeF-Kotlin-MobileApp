package np.com.harishankarsah.fitlife.ui.screen.dashboard.week_plan

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.theme.Error
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.Success
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.viewmodel.WeeklyPlanViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeekPlanScreen(
    onPlanClick: (String) -> Unit,
    viewModel: WeeklyPlanViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Register Dialog
    GlobalDialog()

    // Refresh data when screen appears (handled by flow in init, but kept for safety)
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyPlans()
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

        ) {
            Text(
                text = "My Weekly Schedule",
                style = MaterialTheme.typography.headlineLarge,
                color = OnPrimary
            )

//            GlobalIconButton(
//                icon = Icons.Filled.Add,
//                contentDescription = "Add Workout",
//                onClick = {
//                    // Add workout action
//                }
//            )
        }
        Spacer(modifier = Modifier.height(Size.sm))
        GlobalTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = "Search by exercise name or day",
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
            val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                days.forEach { day ->
                    val dayPlans = state.plans.filter { it.day.equals(day, ignoreCase = true) }
                    
                    if (dayPlans.isNotEmpty()) {
                        item {
                            Text(
                                text = day,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        items(dayPlans, key = { it.id }) { plan ->
                            val dismissState = rememberDismissState(
                                confirmStateChange = {
                                    if (it == DismissValue.DismissedToEnd) {
                                        // Swipe Right -> Set completed
                                        viewModel.markPlanCompleted(plan)
                                        if(plan.completed){
                                            GlobalDialogState.showInfo(
                                                msg = "You already have completed this task."
                                            )
                                        }else{
                                            GlobalDialogState.showSuccess(
                                                msg = "Congratulations! You have completed this task."
                                            )
                                        }


                                        false
                                    } else if (it == DismissValue.DismissedToStart) {
                                        // Swipe Left -> Delete with confirmation
                                        GlobalDialogState.showConfirmation(
                                            title = "Delete Plan",
                                            message = "Are you sure you want to delete '${plan.exerciseName}'?",
                                            onConfirm = { viewModel.deletePlan(plan.id) }
                                        )
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
                                        DismissDirection.StartToEnd -> Icons.Default.CheckCircle
                                        DismissDirection.EndToStart -> Icons.Default.Delete
                                        null -> Icons.Default.Add // Placeholder
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 8.dp), // Adjust for item spacing if needed, but item spacing is on parent
                                        contentAlignment = alignment
                                    ) {
                                        // Background color container
                                        Surface(
                                            modifier = Modifier.fillMaxSize(),
                                            color = color,
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Box(
                                                contentAlignment = alignment,
                                                modifier = Modifier.padding(horizontal = 20.dp)
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    }
                                },
                                dismissContent = {
                                    WeeklyPlanItem(
                                        plan = plan,
                                        onClick = { onPlanClick(plan.id) },
                                        onToggleComplete = { viewModel.toggleCompletion(plan) },
                                        onDelete = {
                                            GlobalDialogState.showConfirmation(
                                                title = "Delete Plan",
                                                message = "Are you sure you want to delete this plan?",
                                                onConfirm = { viewModel.deletePlan(plan.id) }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
                
                if (state.plans.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No plans added yet. Go to exercises to add some!", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyPlanItem(
    plan: WeeklyPlanModel,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val containerColor = if (plan.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (plan.completed) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val subTextColor = if (plan.completed) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else Color.Gray
    val iconColor = if (plan.completed) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!plan.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = plan.imageUrl,
                    contentScale = ContentScale.Crop,
                contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)

                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(5.dp))

            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(
                    text = plan.exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (plan.completed) TextDecoration.LineThrough else TextDecoration.None,
                    color = contentColor
                )
                if (plan.reps > 0 || plan.sets > 0) {
                    Text(
                        text = "${plan.sets} Sets • ${plan.reps} Reps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = subTextColor
                    )
                }
                if (plan.notes.isNotEmpty()) {
                    Text(
                        text = plan.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = subTextColor,
                        maxLines = 2
                    )
                }
            }
            Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = "View",
                    tint = iconColor,
                modifier = Modifier.size(Size.iconSm)
                )
            }

    }
}
