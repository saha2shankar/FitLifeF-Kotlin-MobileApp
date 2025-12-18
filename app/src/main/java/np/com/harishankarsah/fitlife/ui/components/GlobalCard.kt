package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.IncompleteCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalCard(
    title: String,
    subtitle: String? = null,
    description: String? = null,
    imageUrl: String? = null,
    isComplete: Boolean? = null,
    contentScale: ContentScale = ContentScale.Crop,
    iconTint: Color = Primary,
    backgroundColor: Color = Surface,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(16.dp)
) {
    // Adjust card background if completed
    val cardBackgroundColor = if (isComplete == true) Primary.copy(alpha = 0.3f) else backgroundColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = Shapes.small,
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Leading Image
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = contentScale,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelLarge.copy(color = OnSurface.copy(0.8f)),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface.copy(0.7f)),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Completion Icon
            if (isComplete != null) {
                Icon(
                    imageVector = if (isComplete) Icons.Filled.CheckCircle else Icons.Outlined.IncompleteCircle,
                    contentDescription = if (isComplete) "Completed" else "Not completed",
                    tint = if (isComplete) Success else OnSurface.copy(0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalCardVariantsPreview() {
    FitLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Card with background image
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                isComplete = true,
                onClick = {}
            )
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                onClick = {}
            )
            // Simple card with success state
            GlobalCard(
                title = "Meditation Complete",
                subtitle = "10 minutes",
            )

            // Card with background image
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                isComplete = true,
                onClick = {}
            )
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                onClick = {}
            )
            // Card with background image
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                isComplete = true,
                onClick = {}
            )
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                onClick = {}
            )
            // Card with background image
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                isComplete = true,
                onClick = {}
            )
            GlobalCard(
                title = "Workout Challenge",
                subtitle = "30 Day Program",
                description = "Transform your body in 30 days",
                imageUrl = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                onClick = {}
            )

        }
    }
}

