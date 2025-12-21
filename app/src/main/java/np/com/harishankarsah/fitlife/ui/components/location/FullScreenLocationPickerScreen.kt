package np.com.harishankarsah.fitlife.ui.components.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import np.com.harishankarsah.fitlife.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenLocationPickerScreen(
    initialLocation: LatLng,
    onBack: () -> Unit,
    onConfirm: (LatLng) -> Unit
) {
    var selectedLocation by remember { mutableStateOf(initialLocation) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { onConfirm(selectedLocation) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Confirm Location", color = OnPrimary)
            }
        }
    ) { padding ->

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            onMapClick = { selectedLocation = it }
        ) {
            Marker(
                state = MarkerState(position = selectedLocation),
                title = "Selected Location"
            )
        }
    }
}
