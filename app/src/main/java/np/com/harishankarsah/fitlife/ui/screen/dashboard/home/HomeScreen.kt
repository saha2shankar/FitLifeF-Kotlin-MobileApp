package np.com.harishankarsah.fitlife.ui.screen.dashboard.home

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.theme.Surface
import np.com.harishankarsah.fitlife.ui.utils.Size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import np.com.harishankarsah.fitlife.ui.components.GlobalCard
import np.com.harishankarsah.fitlife.ui.components.GlobalCardVariantsPreview
import np.com.harishankarsah.fitlife.ui.components.GlobalGaugeChart
import np.com.harishankarsah.fitlife.ui.components.GlobalGaugeChartPreview
import np.com.harishankarsah.fitlife.ui.components.GlobalImageCard
import np.com.harishankarsah.fitlife.ui.components.GlobalTaskSummaryCard
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.SquareCard
import np.com.harishankarsah.fitlife.ui.components.SquareCardVariant
import np.com.harishankarsah.fitlife.ui.screen.dashboard.MainScreen
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnSecondary
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary
import np.com.harishankarsah.fitlife.ui.theme.Shapes
import np.com.harishankarsah.fitlife.ui.utils.Validation
import np.com.harishankarsah.fitlife.viewmodel.HomeState
import np.com.harishankarsah.fitlife.viewmodel.HomeViewModel
import np.com.harishankarsah.fitlife.viewmodel.ExerciseViewModel
import np.com.harishankarsah.fitlife.viewmodel.ExerciseState
import np.com.harishankarsah.fitlife.model.ExerciseModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import kotlin.text.ifEmpty

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    exerciseViewModel: ExerciseViewModel = viewModel(),
    onNavigateToExercises: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToAccessories: () -> Unit = {}

) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    val homeState by viewModel.state.collectAsState()
    val exerciseState by exerciseViewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    username = doc.getString("name") ?: ""
                    email = doc.getString("email") ?: ""
                    mobile = doc.getString("mobile") ?: ""
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Body content
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            TestCardScreen(
                username = username.ifEmpty { "User" },
                homeState = homeState,
                exerciseState = exerciseState,
                onNavigateToExercises = onNavigateToExercises,
                onNavigateToExpenses = onNavigateToExpenses,
                onNavigateToAccessories =onNavigateToAccessories
            )
        }
    }
}
@Composable
fun TestCardScreen(
    username: String,
    homeState: HomeState,
    exerciseState: ExerciseState,
    onNavigateToExercises: () -> Unit,
    onNavigateToExpenses: () -> Unit,
    onNavigateToAccessories:() ->Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState()) // make it scrollable

    ) {
        var searchtext by remember { mutableStateOf("") }
        GlobalTextField(
            value = searchtext,
            onValueChange = { searchtext = it },
            placeholder = "Search",
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(Shapes.small)
                .shadow(8.dp, RoundedCornerShape(16.dp))
        ) {

            AsyncImage(
                model = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                contentDescription = "Banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.92f) // minimal opacity for premium look
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome "+ username + " !",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Text(
                    text = "Keep pushing toward your goals 💪",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Dashboard Stats",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        DashboardStatsScreen(homeState = homeState)
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Activity Overview",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        DashboardTwoGauges(homeState = homeState)
        Text(text = "Task Summary",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        TaskSummaryScreen(homeState = homeState)
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Recommended Exercise",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        ExercisePreviewSection(
            exercises = exerciseState.exercises.take(3),
            onViewAll = onNavigateToExercises
        )
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Action ",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        SquareCardGrid(
            onNavigateToExpenses = onNavigateToExpenses,
            onNavigateToAccessories = onNavigateToAccessories
        )
        Spacer(modifier = Modifier.height(Size.md))


    }

}
@Composable
fun DashboardTwoGauges(
    homeState: HomeState
) {
    // Handle loading
    if (homeState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Handle error
    if (homeState.error != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = homeState.error,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    val total = homeState.totalWeeklyPlans

    // No data case
    if (total <= 0) {
        Text(
            text = "No weekly data available",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        return
    }

    val completed = homeState.completedWeeklyPlans.coerceAtMost(total)
    val remaining = (total - completed).coerceAtLeast(0)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Weekly Remaining
        GlobalGaugeChart(
            value = remaining.toFloat(),
            minValue = 0f,
            maxValue = total.toFloat(),
            size = 150.dp,
            progressColor = Secondary,
            label = "Weekly\nRemaining",
            animationEasing = CubicBezierEasing(
                0.68f, -0.55f, 0.265f, 1.55f
            )
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Weekly Progress
        GlobalGaugeChart(
            value = completed.toFloat(),
            minValue = 0f,
            maxValue = total.toFloat(),
            size = 150.dp,
            progressColor = Primary,
            showPercentage = true,
            showValue = true,
            showTicks = true,
            animationDuration = 2500,
            animationEasing = CubicBezierEasing(
                0.68f, -0.55f, 0.265f, 1.55f
            ),
            label = "Weekly\nProgress"
        )
    }
}


@Composable
fun DashboardStatsScreen(homeState: HomeState) {
    if (homeState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (homeState.error != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error: ${homeState.error}",
                color = MaterialTheme.colorScheme.error
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Total Weekly Plans Card
            DashboardStatCard(
                title = "Weekly Plans",
                value = homeState.totalWeeklyPlans.toString(),
                modifier = Modifier.weight(1f)
            )
            
            // Completed Weekly Plans Card
            DashboardStatCard(
                title = "Completed",
                value = homeState.completedWeeklyPlans.toString(),
                modifier = Modifier.weight(1f)
            )
            
            // Total Exercises Card
            DashboardStatCard(
                title = "Exercises",
                value = homeState.totalExercises.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TaskSummaryScreen(homeState: HomeState) {
        GlobalTaskSummaryCard(
            totalTasks = homeState.totalWeeklyPlans,
            completedTasks = homeState.completedWeeklyPlans,
            remainingTasks = homeState.totalWeeklyPlans - homeState.completedWeeklyPlans,
        )
}

@Composable
fun ExercisePreviewSection(
    exercises: List<ExerciseModel>,
    onViewAll: () -> Unit
) {
    if (exercises.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "No exercises",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No exercises yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    } else {
        Column {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(exercises, key = { it.id }) { exercise ->
                    ExercisePreviewCard(exercise = exercise)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewAll() },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View All",
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ExercisePreviewCard(exercise: ExerciseModel) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!exercise.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.routineName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 1
            )
            if (exercise.instructions.isNotEmpty()) {
                Text(
                    text = exercise.instructions,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SquareCardGrid(
    onNavigateToExpenses: () -> Unit,
    onNavigateToAccessories: () -> Unit
) {
    FitLifeTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SquareCard(
                title = "Expenses",
                icon = Icons.Default.MonetizationOn,
                variant = SquareCardVariant.Outlined,
                backgroundColor = Secondary,
                viewMoreText = "View more",
                onClick = onNavigateToExpenses
            )
            Spacer(modifier = Modifier.width(16.dp))
            SquareCard(
                title = "Accessories",
                icon = Icons.Default.BuildCircle,
                variant = SquareCardVariant.Outlined,
                viewMoreText = "View more",
                onClick = onNavigateToAccessories
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FitLifeTheme {
        HomeScreen(
        )
    }
}
