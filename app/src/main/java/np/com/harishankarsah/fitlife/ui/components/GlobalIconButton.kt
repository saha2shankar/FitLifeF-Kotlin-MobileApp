package np.com.harishankarsah.fitlife.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.vector.ImageVector
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.Secondary

enum class IconButtonType {
    PRIMARY, SECONDARY, TRANSPARENT
}

@Composable
fun GlobalIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: IconButtonType = IconButtonType.PRIMARY,
    enabled: Boolean = true
) {
    val backgroundColor = when (buttonType) {
        IconButtonType.PRIMARY -> Primary
        IconButtonType.SECONDARY -> Secondary
        IconButtonType.TRANSPARENT -> Color.Transparent
    }

    val iconTint = when (buttonType) {
        IconButtonType.TRANSPARENT -> Primary
        else -> OnPrimary
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}
