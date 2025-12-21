package np.com.harishankarsah.fitlife.ui.screen.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar.BottomNavigationBar
import np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar.Screen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.home.HomeScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.profile.ProfileScreen
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.screen.login.LoginActivity
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import np.com.harishankarsah.fitlife.ui.screen.dashboard.accessories.AccessoriesActivity
import np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise.CreateExerciseActivity
import np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise.ExerciseScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.maps.MapScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.week_plan.WeekPlanScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.week_plan.WeeklyPlanDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import np.com.harishankarsah.fitlife.ui.screen.dashboard.expense.ExpenseActivity

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitLifeTheme {
                MainScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    // Get current route
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
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

    Scaffold(
        topBar = {
            if (currentRoute == Screen.Home.rout) {

                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { /* TODO */ }) {
                            AsyncImage(
                                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtrzwqNKIf5e7fILShRYoSD5EIa6nMheVTEQ&s",
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop, // ensures the image fills the circle
                                modifier = Modifier
                                    .size(40.dp)          // profile size
                                    .clip(CircleShape)
                            )
                        }
                    },
                    title = {
                        Column {
                            Text(text = username.ifEmpty { "User" })
                            Text(
                                text = email.ifEmpty { "user@example.com" },
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {

                            GlobalDialogState.showConfirmation(
                                title = "Logout",
                                message = "Are you sure you want to logout?",
                                confirmText = "Logout"
                            ) {
                                FirebaseAuth.getInstance().signOut()
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                (context as? android.app.Activity)?.finish()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Logout,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        },


        modifier = Modifier.fillMaxSize(),
        containerColor = OnAccent,
        bottomBar = { BottomNavigationBar(navController) },
//        floatingActionButton = {
//            if (currentRoute == Screen.Home.rout) {
//                FloatingActionButton(
//                    onClick = {
//                        val intent = Intent(context, ExpenseActivity::class.java)
//                        context.startActivity(intent)
//                    },
//                    containerColor = MaterialTheme.colorScheme.secondary
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Add",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.rout,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.rout) {
                HomeScreen(
                    onNavigateToExercises = {
                        navController.navigate(Screen.Workout.rout)
                    },
                    onNavigateToExpenses = {
                        val intent = Intent(context, ExpenseActivity::class.java)
                        context.startActivity(intent)

                    },
                    onNavigateToAccessories={
                        val intent = Intent(context, AccessoriesActivity::class.java)
                        context.startActivity(intent)

                    }
                )
            }

            composable(Screen.Workout.rout) {
                ExerciseScreen(
                    onCreateExerciseClick={
                        val intent = Intent(context, CreateExerciseActivity::class.java)
                        context.startActivity(intent)
                })
            }
            composable(Screen.WeekSchedule.rout) {
                WeekPlanScreen(
                    onPlanClick = { planId ->
                        navController.navigate(Screen.WeekScheduleDetail.createRoute(planId))
                    }
                )
            }

            composable(
                route = Screen.WeekScheduleDetail.rout,
                arguments = listOf(navArgument("planId") { type = NavType.StringType })
            ) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId") ?: ""
                WeeklyPlanDetailScreen(
                    planId = planId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Map.rout) {
                MapScreen()
            }

            composable(Screen.Profile.rout) {
                ProfileScreen()
            }
            
            composable(Screen.ExpenseTracking.rout) {
                np.com.harishankarsah.fitlife.ui.screen.dashboard.expense.ExpenseTrackingScreen(
                    onBack = { navController.popBackStack() },
                    onBackclick={}
                    )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
