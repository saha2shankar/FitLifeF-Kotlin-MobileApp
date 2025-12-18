package np.com.harishankarsah.fitlife.ui.screen.dashboard.accessories

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import np.com.harishankarsah.fitlife.ui.screen.dashboard.DashboardActivity
import np.com.harishankarsah.fitlife.ui.screen.forgot.ForgotScreen
import np.com.harishankarsah.fitlife.ui.screen.login.LoginActivity
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.viewmodel.AuthState
import np.com.harishankarsah.fitlife.viewmodel.AuthViewModel

class AccessoriesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitLifeTheme {
                AccessoriesScreen(
                )
        }
    }
    }
    fun openDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}
