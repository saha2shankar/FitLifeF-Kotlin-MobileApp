package np.com.harishankarsah.fitlife.ui.components.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import np.com.harishankarsah.fitlife.ui.utils.Size

@Composable
fun GlobalMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    zoom: Float = 15f,
    markerTitle: String = "Location",
    cardHeight: Dp = 200.dp,
    cardWidth: Dp = Dp.Unspecified,
    cornerRadius: Dp = Size.sm,
    elevation: Dp = 8.dp,
    showTitle: Boolean = true
) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude),
            zoom
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (cardWidth != Dp.Unspecified) Modifier.width(cardWidth) else Modifier)
            .height(cardHeight)
            .shadow(elevation, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = if (showTitle) 0.dp else 0.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = false
                )
            ) {
                Marker(
                    state = MarkerState(position = LatLng(latitude, longitude)),
                    title = markerTitle
                )
            }
        }
    }
}
