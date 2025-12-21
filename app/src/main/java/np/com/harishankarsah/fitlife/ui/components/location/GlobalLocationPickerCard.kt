package np.com.harishankarsah.fitlife.ui.components.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalLocationPickerCard(
    modifier: Modifier = Modifier,
    selectedLocation: LatLng?,
    title: String = "Select Location",
    onClick: () -> Unit
) {
Column {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = OnSurface
    )

    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = Shapes.small,
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = OnSurface
                )
                Text(
                    text = selectedLocation?.let {
                        "Lat: ${it.latitude}, Lng: ${it.longitude}"
                    } ?: "Tap to choose location",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
}
