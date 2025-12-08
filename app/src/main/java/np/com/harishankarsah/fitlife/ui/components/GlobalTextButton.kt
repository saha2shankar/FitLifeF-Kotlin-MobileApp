package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary

enum class TextButtonType {
    PRIMARY, SECONDARY
}

@Composable
fun GlobalTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: TextButtonType = TextButtonType.PRIMARY,
    enabled: Boolean = true
) {
    val textColor = when (buttonType) {
        TextButtonType.PRIMARY -> Primary
        TextButtonType.SECONDARY -> Secondary
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
