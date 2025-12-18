package np.com.harishankarsah.fitlife.ui.screen.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import np.com.harishankarsah.fitlife.ui.screen.forgot.ForgotActivity
import np.com.harishankarsah.fitlife.ui.screen.login.LoginActivity
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.screen.dashboard.DashboardActivity
import np.com.harishankarsah.fitlife.viewmodel.AuthState
import np.com.harishankarsah.fitlife.viewmodel.AuthViewModel

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            FitLifeTheme {
               SignupScreen(
                   onSignInClicked = {
                       openSignInActivity()
                   },
                   onSignupClicked = { name, email, mobile, password ->
                       authViewModel.register(name, email, mobile, password)
                   }
               )
            }
        }
        authViewModel.authState.observe(this, Observer { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    AlertDialog.Builder(this)
                        .setTitle("Registration Successful")
                        .setMessage("Your account has been created.")
                        .setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .show()
                }
                is AuthState.Error -> {
                    AlertDialog.Builder(this)
                        .setTitle("Registration Failed")
                        .setMessage(state.message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
                else -> {}
            }
        })
    }
    fun openSignInActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
