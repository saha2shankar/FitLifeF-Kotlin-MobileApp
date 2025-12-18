package np.com.harishankarsah.fitlife.ui.screen.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar.BottomNavigationBar
import np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar.Screen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.home.HomeScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.profile.ProfileScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.search.SearchScreen
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
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import np.com.harishankarsah.fitlife.ui.screen.dashboard.accessories.AccessoriesScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.maps.MapScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.workplan.WorkOutPlanScreen


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
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = OnAccent,
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "Add clicked!", Toast.LENGTH_SHORT).show() },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.rout,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.rout) {
                HomeScreen(
                    onLogoutClick = {
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
                    }
                )
            }

            composable(Screen.Workout.rout) {
                WorkOutPlanScreen()
            }

            composable(Screen.Map.rout) {
                MapScreen()
            }

            composable(Screen.Profile.rout) {
                ProfileScreen()
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
