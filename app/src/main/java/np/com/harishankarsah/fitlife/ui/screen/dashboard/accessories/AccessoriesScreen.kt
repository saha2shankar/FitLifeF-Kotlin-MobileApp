package np.com.harishankarsah.fitlife.ui.screen.dashboard.accessories

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.components.ButtonType
import np.com.harishankarsah.fitlife.ui.components.GlobalButton
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.IconButtonType
import np.com.harishankarsah.fitlife.ui.components.ShareDelegationBottomSheet
import np.com.harishankarsah.fitlife.ui.components.ShareDelegationType
import np.com.harishankarsah.fitlife.model.ExerciseModel
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import np.com.harishankarsah.fitlife.ui.components.SquareCard
import np.com.harishankarsah.fitlife.ui.components.SquareCardVariant
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.Secondary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.ui.utils.Validation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.screen.dashboard.DashboardActivity


@Composable
fun AccessoriesScreen(
    onBackclick: () -> Unit
    ) {
    val context = LocalContext.current
    var showShareBottomSheet by remember { mutableStateOf(false) }
    
    // Extract equipment list from accessories
    val equipmentList = remember {
        listOf(
            "Dumbbell", "Kettlebell", "Yoga Mat", "Resistance Band",
            "Jump Rope", "Medicine Ball", "Pull-up Bar", "Foam Roller",
            "Gym Gloves", "Ab Wheel", "Exercise Ball", "Weight Plates",
            "Treadmill", "Elliptical", "Stationary Bike", "Stepper"
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        GlobalIconButton(
            icon = Icons.Default.ArrowBackIos,
            contentDescription = "Back",
            onClick = { onBackclick() },
            buttonType = IconButtonType.PRIMARY
        )
        Spacer(modifier = Modifier.height(Size.md))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Accessories Items",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { showShareBottomSheet = true }) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share / Delegate",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
            Spacer(modifier = Modifier.height(Size.sm))
            // Description
            Text(
                text = "Explore these essential accessories to help you improve your workouts and fitness goals.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        Spacer(modifier = Modifier.height(Size.md))
        AccessoriesCardItems()
        Spacer(modifier = Modifier.height(Size.xxl))
        Spacer(modifier = Modifier.height(Size.xxl))


    }

    // Share Delegation Bottom Sheet
    if (showShareBottomSheet) {
        val dummyExercise = ExerciseModel(
            routineName = "Accessories List",
            equipment = equipmentList,
            instructions = "Check out these accessories!"
        )
        ShareDelegationBottomSheet(
            exercise = dummyExercise,
            onDismissRequest = { showShareBottomSheet = false }
        )
    }
}

@Composable
fun AccessoriesCardItems() {
    // Sample data for 20 items
    val accessories = listOf(
        "Dumbbell" to "https://img.freepik.com/free-psd/top-view-dumbbell-isolated_23-2151849414.jpg",
        "Kettlebell" to "https://www.marbo-sport.pl/hpeciai/7eb9fbb3d6e9c6bb308131dfb1e66809/pol_pl_29819-29819_1.jpg",
        "Yoga Mat" to "https://i5.walmartimages.com/seo/BalanceFrom-All-Purpose-1-2-In-High-Density-Foam-Exercise-Yoga-Mat-Anti-Tear-with-Carrying-Strap-Blue_f4b717d9-6dc1-4198-b0f8-3e88d8a2d09f.02e132731c4b639cee0057c099b45f37.jpeg",
        "Resistance Band" to "https://motv8.ca/cdn/shop/files/IMG-7960.png?v=1710383460",
        "Jump Rope" to "https://contents.mediadecathlon.com/p2568401/k\$095d4e977e83998ef2ffcaf50953b94b/rubber-skipping-rope-jr500.jpg",
        "Medicine Ball" to "https://sportscenter.com.np/wp-content/uploads/2021/06/slam-Ball-Vector-X-4-1.jpg",
        "Pull-up Bar" to "https://bellsofsteel.com/cdn/shop/files/R1018-carousel-lifestyle-1.jpg?v=1764590696&width=2048",
        "Foam Roller" to "https://static-01.daraz.com.np/p/b0c95dea2cb8e305d284986b6ccce37d.jpg",
        "Gym Gloves" to "https://static-01.daraz.com.np/p/5601e33214d7cb38527b716107cd6aaa.jpg",
        "Ab Wheel" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1QQtJqOsJewTrsHEds9qYmTo0yBsSn5HC4Q&s",
        "Exercise Ball" to "https://static-01.daraz.com.np/p/11492d4376ba7ebbb2fb0844a68f47d9.jpg",
        "Weight Plates" to "https://m.media-amazon.com/images/I/71QxMqogSwL._AC_SL1500_.jpg",
        "Treadmill" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGOKiWq-6sL7iorV1JlmdGAXigiW_o6oudGA&s",
        "Elliptical" to "https://sunnyhealthfitness.com/cdn/shop/files/sunny-health-fitness-ellipticals-smart-elliptical-machine-sf-e3889SMART-01_750x.jpg?v=1679674842",
        "Stationary Bike" to "https://img.drz.lazcdn.com/static/np/p/3c73b4e6a41f3cf5851f4f2de276d443.jpg_720x720q80.jpg",
        "Stepper" to "https://img.drz.lazcdn.com/static/np/p/e5044ce05baa707498c592c2673a9d74.jpg_720x720q80.jpg",
          )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 items per row
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(accessories) { item ->
            SquareCard(
                title = item.first,
                imageUrl = item.second,
                variant = SquareCardVariant.Outlined,
                backgroundColor = Secondary,
                viewMoreText = null,
            )
        }
    }

}
