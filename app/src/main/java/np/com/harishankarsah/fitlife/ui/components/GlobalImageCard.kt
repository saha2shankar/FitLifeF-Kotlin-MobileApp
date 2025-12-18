package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.ui.theme.OnSurface
import np.com.harishankarsah.fitlife.ui.theme.Surface

@Composable
fun GlobalImageCard(
    title: String? = null,
    subtitle: String? = null,
    description: String? = null,
    imageRes: Int? = null,            // For local drawable
    imageUrl: String? = null,         // For Coil loading
    contentScale: ContentScale = ContentScale.Crop,
    backgroundColor: Color = Surface,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column {

            // --- IMAGE SECTION ---
            when {
                imageRes != null -> {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                imageUrl != null -> {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }

            // --- TEXT SECTION ---
            Column(modifier = Modifier.padding(16.dp)) {
                if (title != null) {   Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
                    }

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = OnSurface.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = OnSurface.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
