package np.com.harishankarsah.fitlife.ui.screen.forgot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.components.ButtonType
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.IconButtonType
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.ui.utils.Validation


@Composable
fun ForgotScreen(
    onSignInClicked: () -> Unit,
    onSendResetClicked: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(Size.xxl))
        GlobalIconButton(
            icon = Icons.Default.ArrowBackIos,
            contentDescription = "Back",
            onClick = {onSignInClicked()},
            buttonType = IconButtonType.PRIMARY
        )
        Spacer(modifier = Modifier.height(Size.lg))


        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(Size.md))
        Text(
            text = "Enter your informations below or login with a other account.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(Size.md))
        GlobalTextField(
            value = email,
            isRequired = true,
            onValueChange = { email = it },
            error = Validation.validateEmail(email),
            placeholder = "Email",
        )
        Spacer(modifier = Modifier.height(Size.lg))
        Box(
                modifier = Modifier
                    .fillMaxWidth(),
        contentAlignment = Alignment.Center
        ) {
        Text(
            text = "Try another way",
            style = MaterialTheme.typography.titleSmall,
            color = OnPrimary,
            modifier = Modifier.clickable {
                // Navigate to Register Screen
            }
        )
    }
        Spacer(modifier = Modifier.height(Size.lg))
        GlobalButton(
            text = "Send",
            onClick = {
                val emailError = Validation.validateEmail(email)
                if (emailError == null && email.isNotEmpty()) {
                    onSendResetClicked(email)
                }
            },
            buttonType = ButtonType.PRIMARY
        )

    }
}
