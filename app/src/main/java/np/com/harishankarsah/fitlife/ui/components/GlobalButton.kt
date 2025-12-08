package np.com.harishankarsah.fitlife.ui.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.OnSecondary
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary
import np.com.harishankarsah.fitlife.ui.theme.Shapes

// Type of Button
enum class ButtonType {
    PRIMARY, SECONDARY, OUTLINED
}

@Composable
fun GlobalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.PRIMARY,
    enabled: Boolean = true
) {
    when (buttonType) {
        ButtonType.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.fillMaxWidth().height(53.dp),
                shape = Shapes.small,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(text = text, color = OnPrimary, style = MaterialTheme.typography.labelLarge)
            }
        }
        ButtonType.SECONDARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                shape = Shapes.small,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(containerColor = Secondary)
            ) {
                Text(text = text, color = OnSecondary, style = MaterialTheme.typography.labelLarge)
            }
        }
        ButtonType.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                shape = Shapes.small,
                enabled = enabled,
                border = BorderStroke(1.dp, Primary)
            ) {
                Text(text = text, color = Primary, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
