package np.com.harishankarsah.fitlife.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.components.ButtonType
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.ui.utils.Validation


@Composable
fun LoginScreen(
    modifier: Modifier= Modifier,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginClicked: (String, String) -> Unit,
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val usernameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    var isSubmitted by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val usernameError =
        if (isSubmitted && username.isBlank()) "Username is required"
        else Validation.validateEmail(username)

    val passwordError =
        if (isSubmitted && password.isBlank()) "Password is required"
        else Validation.validatePassword(password)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "FitLife Logo",
            modifier = Modifier
                .size(120.dp)
        )
        // Title
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(Size.md))

        Text(
            text = "Login to continue your fitness journey",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(Size.md))
        GlobalTextField(
            value = username,
            isRequired = true,
            onValueChange = { username = it },
            error = usernameError,
            label = "Username",
            placeholder = "Enter your username",
            modifier = Modifier.focusRequester(usernameFocus),
            leadingIcon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Username",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md) )

        // Password Field
        GlobalTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter your password",
            isRequired = true,
            isPassword = true,
            error = passwordError,
            modifier = Modifier.focusRequester(passwordFocus),
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "Password",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )

        Spacer(modifier = Modifier.height(Size.lg))

        // LOGIN BUTTON (only active if valid)
        GlobalButton(
            text = "Login",
            onClick = {
                isSubmitted = true
                focusManager.clearFocus()

                when {
                    username.isBlank() -> {
                        usernameFocus.requestFocus()
                    }

                    password.isBlank() -> {
                        passwordFocus.requestFocus()
                    }

                    usernameError == null && passwordError == null -> {
                        onLoginClicked(username.trim(), password)
                    }
                }
            },
            buttonType = ButtonType.PRIMARY
        )


        Spacer(modifier = Modifier.height(Size.lg))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Create Account?",
                style = MaterialTheme.typography.titleSmall,
                color = OnPrimary,
                modifier = Modifier.clickable() {
                    onSignUpClicked()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "|",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            GlobalTextButton(
                text = "Forgot Password?",
                onClick = {
                    onForgotPasswordClicked()
                },
                buttonType = TextButtonType.PRIMARY
            )

        }

    }
}
