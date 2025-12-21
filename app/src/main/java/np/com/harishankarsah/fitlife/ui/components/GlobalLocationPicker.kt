package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalLocationPicker(
    modifier: Modifier = Modifier,
    initialLocation: LatLng = LatLng(27.7172, 85.3240), // Kathmandu default
    title: String = "Pick Location",
    mapHeight: Dp = 300.dp,
    onLocationPicked: (LatLng) -> Unit
) {

    var selectedLocation by remember { mutableStateOf(initialLocation) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Map
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    selectedLocation = it
                    onLocationPicked(it)
                }
            ) {
                Marker(
                    state = MarkerState(position = selectedLocation),
                    title = "Selected Location"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Coordinates preview
            Text(
                text = "Lat: ${selectedLocation.latitude}, Lng: ${selectedLocation.longitude}",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface.copy(alpha = 0.7f)
            )
        }
    }
}
