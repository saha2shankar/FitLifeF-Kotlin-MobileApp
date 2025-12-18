package np.com.harishankarsah.fitlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.compose.ui.text.font.FontWeight
import np.com.harishankarsah.fitlife.ui.components.GlobalCard
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FitLifeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SplashScreenContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreenContent() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Swipe Left Or Right") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.Outlined.Menu,
                            contentDescription = "Navigation Menu"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(20) { index ->
                SwipeLeftRight(index = index)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeLeftRight(index: Int) {
    val squareSize = 60.dp
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1, -sizePx to 2)

    val openDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Alert Dialog
        CustomAlertDialog(openDialog = openDialog, text = dialogText.value)

        val backgroundColor = if (swipeableState.offset.value < 0) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.primary
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        openDialog.value = true
                        dialogText.value = "Do you want to edit item ${index + 1}"
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White,
                    )
                }

                IconButton(
                    onClick = {
                        openDialog.value = true
                        dialogText.value = "Do you want to delete item ${index + 1}"
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.White,
                    )
                }
            }

            // Card with content
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(swipeableState.offset.value.roundToInt(), 0)
                    }
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .align(Alignment.CenterStart)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        GlobalCard(
                            title = "Workout Challenge ${index + 1}",
                            subtitle = "30 Day Program ${index + 1}",
                            description = "Transform your body in 30 days ${index + 1}",
                            isComplete = listOf(true, false).random(),
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialog(openDialog: MutableState<Boolean>, text: String) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Alert!", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = { Text(text) },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}
