package np.com.harishankarsah.fitlife.ui.screen.signup

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.components.ButtonType
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.screen.forgot.ForgotScreen
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.ui.utils.Validation


@Composable
fun SignupScreen(
    onSignInClicked: () -> Unit,

    ) {
    var full_name by remember { mutableStateOf("") }
    var mobile_number by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(Size.xxl))
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(Size.md))

        Text(
            text = "Welcome! Build your fitness journey with structure.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(Size.md))
        GlobalTextField(
            value = full_name,
            isRequired = true,
            onValueChange = { full_name = it },
            error = Validation.validateName(full_name),
            label = "Full Name",
            placeholder = "Enter your full name",
            leadingIcon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Username",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))
        GlobalTextField(
            value = email,
            isRequired = true,
            onValueChange = { email = it },
            error = Validation.validateEmail(email),
            label = "Email",
            placeholder = "Enter your email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))
        GlobalTextField(
            value = mobile_number,
            isRequired = true,
            onValueChange = { mobile_number = it },
            error = Validation.validatePhone(mobile_number),
            label = "Mobile Number",
            placeholder = "Enter your mobile number",
            minLines = 9,
            maxLines = 10,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        leadingIcon = {
                Icon(
                    Icons.Filled.Phone,
                    contentDescription = "Mobile Number",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))
        // Password Field
        GlobalTextField(
            value = password,
            isRequired = true,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter your password",
            isPassword = true,
            error = Validation.validatePassword(password),
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "Password",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))
        // Password Field
        GlobalTextField(
            value = cpassword,
            isRequired = true,
            onValueChange = { cpassword = it },
            label = "Confirm Password",
            placeholder = "Enter confirm password",
            isPassword = true,
            error = Validation.validatePassword(cpassword),
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "CPassword",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )

        Spacer(modifier = Modifier.height(Size.lg))

        // LOGIN BUTTON (only active if valid)
        GlobalButton(
            text = "Signup",
            onClick = {

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
                text = "Have an Account?",
                style = MaterialTheme.typography.titleSmall,
                color = OnPrimary,
            )
            GlobalTextButton(
                text = "Login",
                onClick = { onSignInClicked()},
                buttonType = TextButtonType.PRIMARY
            )

        }
    }
}

