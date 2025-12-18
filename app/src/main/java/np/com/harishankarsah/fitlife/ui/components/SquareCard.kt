package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Shapes
import np.com.harishankarsah.fitlife.ui.theme.Surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import np.com.harishankarsah.fitlife.ui.screen.dashboard.home.HomeScreen
import np.com.harishankarsah.fitlife.ui.screen.dashboard.home.SquareCardGrid
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale

@Composable
fun SquareCard(
    title: String,
    icon: ImageVector? = null,            // optional icon
    imageUrl: String? = null,             // optional image URL
    modifier: Modifier = Modifier,
    variant: SquareCardVariant = SquareCardVariant.Filled,
    backgroundColor: Color = Surface,
    iconTint: Color = Primary,
    viewMoreText: String? = "View More", // optional
    onClick: (() -> Unit)? = null        // optional
) {
    val border = if (variant == SquareCardVariant.Outlined) {
        BorderStroke(1.dp, Primary.copy(alpha = 0.4f))
    } else null

    Card(
        modifier = modifier
            .size(170.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        shape = Shapes.medium,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = if (variant == SquareCardVariant.Filled)
                backgroundColor else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (variant == SquareCardVariant.Filled) 4.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                            colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.surface,
                    blendMode = BlendMode.DstIn // or BlendMode.Modulate
                )

                )
                Spacer(modifier = Modifier.height(8.dp))
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(5.dp))
            viewMoreText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
            }

        }
    }
}


/**
 * Card visual styles
 */
enum class SquareCardVariant {
    Filled,
    Outlined
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FitLifeTheme {
        SquareCardGrid(
        )
    }
}
