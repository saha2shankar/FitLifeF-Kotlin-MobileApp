package np.com.harishankarsah.fitlife.ui.screen.login

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
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.screen.forgot.ForgotActivity
import np.com.harishankarsah.fitlife.ui.screen.signup.SignupActivity
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.screen.dashboard.DashboardActivity
import np.com.harishankarsah.fitlife.viewmodel.AuthState
import np.com.harishankarsah.fitlife.viewmodel.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            FitLifeTheme {
                LoginScreen(
                    onForgotPasswordClicked = {
                        openForgotActivity()
                    },
                    onSignUpClicked = {
                        openSignUpActivity()
                    },
                    onLoginClicked = { email, password ->
                        authViewModel.login(email, password)
                    }
                )
            }
        }

        authViewModel.authState.observe(this, Observer { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    GlobalDialogState.showSuccess(
                        "Login Successfully Welcome to FitLife 🎉"
                    )

                    finish()
                }
                is AuthState.Error -> {
                    GlobalDialogState.showError(state.message)
                }
                else -> {}
            }
        })
    }

    fun openSignUpActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun openForgotActivity() {
        val intent = Intent(this, ForgotActivity::class.java)
        startActivity(intent)
    }
}
