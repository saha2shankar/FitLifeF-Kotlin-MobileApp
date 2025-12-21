package np.com.harishankarsah.fitlife.ui.screen.dashboard.week_plan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

class WeekPlanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitLifeTheme {
                WeekPlanScreen(
                    onPlanClick = { planId ->
                        // Handle click, maybe show a toast or navigate if this activity supports it
                    }
                )
            }
        }
    }
}

