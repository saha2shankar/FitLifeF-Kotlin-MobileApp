package np.com.harishankarsah.fitlife.ui.screen.dashboard.exercise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import np.com.harishankarsah.fitlife.ui.screen.dashboard.DashboardActivity
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

class ExerciseDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exerciseId = intent.getStringExtra("exercise_id") ?: return finish()

        setContent {
            FitLifeTheme {
                ExerciseDetailScreen(
                    exerciseId = exerciseId,
                    onBack = { finish() }
                )
            }
        }
    }
}
