package np.com.harishankarsah.fitlife.ui.screen.dashboard.buttonbar

sealed class Screen(val rout: String) {
    object Home: Screen("home_screen")
    object Workout: Screen("search_screen")
    object WeekSchedule: Screen("week_schedule_screen")
    object Map: Screen("work_out_plan_screen")
    object Profile: Screen("profile_screen")
    object WeekScheduleDetail: Screen("week_schedule_detail_screen/{planId}") {
        fun createRoute(planId: String) = "week_schedule_detail_screen/$planId"
    }
    object ExpenseTracking: Screen("expense_tracking_screen")
}