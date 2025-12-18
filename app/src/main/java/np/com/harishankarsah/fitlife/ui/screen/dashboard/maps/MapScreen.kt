package np.com.harishankarsah.fitlife.ui.screen.dashboard.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.maps.android.compose.MapType
import np.com.harishankarsah.fitlife.ui.theme.Background
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Shapes

data class FitnessLocation(
    val name: String,
    val position: LatLng,
    val type: LocationType,
    val description: String,
    val icon: ImageVector,
    val address: String
)

enum class LocationType {
    GYM, PARK, TRACK, POOL, YOGA
}

@Composable
fun MapScreen() {
    val kathmanduCenter = LatLng(27.7172, 85.3240)
    val context = LocalContext.current
    var selectedLocation by rememberSaveable { mutableStateOf<FitnessLocation?>(null) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var showRoute by remember { mutableStateOf(false) }

    // Simulated user location in Kathmandu (Thamel area)
    val simulatedUserLocation = LatLng(27.7150, 85.3125)

    val fitnessLocations = remember {
        listOf(
            FitnessLocation(
                name = "Ultimate Fitness Gym",
                position = LatLng(27.7160, 85.3260),
                type = LocationType.GYM,
                description = "24/7 Gym with full equipment",
                icon = Icons.Default.FitnessCenter,
                address = "Thamel, Kathmandu"
            ),
            FitnessLocation(
                name = "Garden of Dreams",
                position = LatLng(27.7145, 85.3120),
                type = LocationType.PARK,
                description = "Perfect for outdoor yoga & jogging",
                icon = Icons.Default.Park,
                address = "Keshar Mahal, Kathmandu"
            ),
            FitnessLocation(
                name = "Dashrath Stadium Track",
                position = LatLng(27.6991, 85.2845),
                type = LocationType.TRACK,
                description = "Olympic standard running track",
                icon = Icons.Default.DirectionsRun,
                address = "Tripureshwor, Kathmandu"
            ),
            FitnessLocation(
                name = "Kathmandu Sports Club",
                position = LatLng(27.7205, 85.3310),
                type = LocationType.POOL,
                description = "Swimming pool & tennis courts",
                icon = Icons.Default.SportsTennis,
                address = "Lazimpat, Kathmandu"
            ),
            FitnessLocation(
                name = "Himalayan Yoga Center",
                position = LatLng(27.7102, 85.3175),
                type = LocationType.YOGA,
                description = "Traditional yoga classes",
                icon = Icons.Default.FitnessCenter,
                address = "Boudha, Kathmandu"
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kathmanduCenter, 13f)
    }

    // Simulate getting user location
    LaunchedEffect(Unit) {
        delay(1000)
        userLocation = simulatedUserLocation
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            myLocationButtonEnabled = true,
            mapToolbarEnabled = true
        )
    }

    val mapProperties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            isBuildingEnabled = true
        )
    }

    // Function to open Google Maps for navigation
    fun openDirections(destination: LatLng) {
        val uri = Uri.parse("google.navigation:q=${destination.latitude},${destination.longitude}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

    // Function to calculate route points (simplified)
    fun calculateRoutePoints(start: LatLng, end: LatLng): List<LatLng> {
        return listOf(
            start,
            LatLng((start.latitude + end.latitude) / 2, (start.longitude + end.longitude) / 2),
            end
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties,
            onMapClick = {
                selectedLocation = null
                showRoute = false
            }
        ) {
            // User location marker
            userLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "Your Location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }

            // Fitness location markers
            fitnessLocations.forEach { location ->
                Marker(
                    state = MarkerState(position = location.position),
                    title = location.name,
                    snippet = location.description,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        when (location.type) {
                            LocationType.GYM -> BitmapDescriptorFactory.HUE_RED
                            LocationType.PARK -> BitmapDescriptorFactory.HUE_GREEN
                            LocationType.TRACK -> BitmapDescriptorFactory.HUE_BLUE
                            LocationType.POOL -> BitmapDescriptorFactory.HUE_CYAN
                            LocationType.YOGA -> BitmapDescriptorFactory.HUE_VIOLET
                        }
                    ),
                    onClick = {
                        selectedLocation = location
                        showRoute = false
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(location.position, 15f)
                        )
                        true
                    }
                )
            }

            // Draw route if showing
            if (showRoute && selectedLocation != null && userLocation != null) {
                val routePoints = calculateRoutePoints(userLocation!!, selectedLocation!!.position)
                Polyline(
                    points = routePoints,
                    color = Color.Blue,
                    width = 8f
                )
            }
        }

        // Top info card
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = Shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = Background
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Find Your Fitness Spot",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap on a location for directions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bottom selected location card
        selectedLocation?.let { location ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = Shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = Background
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = location.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = location.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = location.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = location.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                showRoute = !showRoute
                                if (showRoute && userLocation != null) {
                                    cameraPositionState.move(
                                        CameraUpdateFactory.newLatLngBounds(
                                            com.google.android.gms.maps.model.LatLngBounds.Builder()
                                                .include(userLocation!!)
                                                .include(location.position)
                                                .build(),
                                            100
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showRoute) MaterialTheme.colorScheme.secondary
                                else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (showRoute) "Hide Route" else "Show Route")
                        }

                        Button(
                            onClick = { openDirections(location.position) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Icon(
                                Icons.Default.Directions,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Navigate")
                        }
                    }
                }
            }
        }

        // Location type legend
        if (selectedLocation == null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Legend",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LegendItem(
                        color = Color.Red,
                        text = "Gyms",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    LegendItem(
                        color = Color.Green,
                        text = "Parks",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    LegendItem(
                        color = Color.Blue,
                        text = "Tracks",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    LegendItem(
                        color = Color.Cyan,
                        text = "Pools",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    LegendItem(
                        color = Color.Magenta,
                        text = "Yoga Centers",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LegendItem(
                        color = Color(0xFF1E88E5),
                        text = "Your Location",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(
    color: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}