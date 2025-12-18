package np.com.harishankarsah.fitlife.ui.screen.dashboard.home

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.theme.Surface
import np.com.harishankarsah.fitlife.ui.utils.Size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import np.com.harishankarsah.fitlife.ui.components.GlobalCard
import np.com.harishankarsah.fitlife.ui.components.GlobalGaugeChart
import np.com.harishankarsah.fitlife.ui.components.GlobalGaugeChartPreview
import np.com.harishankarsah.fitlife.ui.components.GlobalImageCard
import np.com.harishankarsah.fitlife.ui.components.GlobalTaskSummaryCard
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.SquareCard
import np.com.harishankarsah.fitlife.ui.components.SquareCardVariant
import np.com.harishankarsah.fitlife.ui.screen.dashboard.MainScreen
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnSecondary
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary
import np.com.harishankarsah.fitlife.ui.theme.Shapes
import np.com.harishankarsah.fitlife.ui.utils.Validation
import kotlin.text.ifEmpty

@Composable
fun HomeScreen(modifier: Modifier= Modifier, onLogoutClick: () -> Unit,) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    username = doc.getString("name") ?: ""
                    email = doc.getString("email") ?: ""
                    mobile = doc.getString("mobile") ?: ""
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopUserBar(
            username = username.ifEmpty { "User" },
            email = email.ifEmpty { "" },
            mobile = mobile.ifEmpty { "" },
            onLogoutClick = {
                onLogoutClick()
            }
        )

        // Body content
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            TestCardScreen(
                username = username.ifEmpty { "User" },
            )
        }
    }
}

@Composable
fun TopUserBar(
    username: String,
    email: String,
    mobile: String,
    onLogoutClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
        modifier = Modifier.height(55.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtrzwqNKIf5e7fILShRYoSD5EIa6nMheVTEQ&s",
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop, // ensures the image fills the circle
                modifier = Modifier
                    .size(35.dp)          // profile size
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Username + Email
            Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()) // make it scrollable
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnAccent
                )
                if (mobile.isNotEmpty()) {
                    Text(
                        text = mobile,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Logout Icon
            IconButton(onClick = onLogoutClick) {
                Icon(
                    modifier = Modifier.size(Size.iconSm),
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                )
            }
        }
    }
}


@Composable
fun TestCardScreen(
    username: String

) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState()) // make it scrollable

    ) {
        var searchtext by remember { mutableStateOf("") }
        GlobalTextField(
            value = searchtext,
            onValueChange = { searchtext = it },
            placeholder = "Search",
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.md))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(Shapes.small)
                .shadow(8.dp, RoundedCornerShape(16.dp))
        ) {

            AsyncImage(
                model = "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
                contentDescription = "Banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.92f) // minimal opacity for premium look
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome "+ username + " !",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Text(
                    text = "Keep pushing toward your goals 💪",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Activity Overview",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        DashboardTwoGauges()
        Text(text = "Task Summary",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        TaskSummaryScreen()
        Spacer(modifier = Modifier.height(Size.md))
        Text(text = "Action ",
            style = MaterialTheme.typography.titleSmall,
            color = OnSecondary
        )
        Spacer(modifier = Modifier.height(Size.md))
        SquareCardGrid()
        Spacer(modifier = Modifier.height(Size.md))


    }

}
@Composable
fun DashboardTwoGauges() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center, // center the gauges
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Gauge
        GlobalGaugeChart(
            value = 75f,
            size = 150.dp,
            progressColor = Secondary,
            label = "Heart Rate",
            animationEasing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f),
        )

        Spacer(modifier = Modifier.width(24.dp)) // gap between gauges

        // Right Gauge
        GlobalGaugeChart(
            value = 92f,
            minValue = 0f,
            maxValue = 100f,
            size = 150.dp,
            progressColor = Primary,
            showPercentage = true,
            showValue = true,
            showTicks = true,
            animationDuration = 2500,
            animationEasing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f),
            label = "OVERALL"
        )
    }
}


@Composable
fun TaskSummaryScreen() {
        GlobalTaskSummaryCard(
            totalTasks = 20,
            completedTasks = 12,
            remainingTasks = 8,
        )
}

@Composable
fun SquareCardGrid() {
    FitLifeTheme {
        Row(
        ) {
            SquareCard(
                title = "Expenses",
                icon = Icons.Default.MonetizationOn,
                variant = SquareCardVariant.Outlined,
                backgroundColor = Secondary,
                viewMoreText = "View more",
                onClick = {}
            )
            Spacer(modifier = Modifier.width(16.dp))
            SquareCard(
                title = "Accessories",
                icon = Icons.Default.BuildCircle,
                variant = SquareCardVariant.Outlined,
                viewMoreText = "View more",
                onClick = {}
            )

        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FitLifeTheme {
        HomeScreen(
            onLogoutClick = {}
        )
    }
}
