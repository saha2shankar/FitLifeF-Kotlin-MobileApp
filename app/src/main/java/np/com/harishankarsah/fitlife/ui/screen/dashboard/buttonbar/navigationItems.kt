package np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar

import np.com.harishankarsah.fitlife.R
import np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar.Screen

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = R.drawable.ic_home,
        route = Screen.Home.rout
    ),
    NavigationItem(
        title = "Workout",
        icon = R.drawable.ic_workout,
        route = Screen.Workout.rout
    ),
    NavigationItem(
        title = "Map",
        icon = R.drawable.ic_map,
        route = Screen.Map.rout
    ),
    NavigationItem(
        title = "Profile",
        icon = R.drawable.ic_profile,
        route = Screen.Profile.rout
    )
)


