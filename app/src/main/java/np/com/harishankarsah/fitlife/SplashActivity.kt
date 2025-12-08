package np.com.harishankarsah.fitlife

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitLifeTheme {
                SplashScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    FitLifeTheme {
        SplashScreen()
    }
}
