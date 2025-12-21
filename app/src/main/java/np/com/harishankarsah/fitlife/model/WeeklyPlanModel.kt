package np.com.harishankarsah.fitlife.model

import com.google.firebase.firestore.PropertyName

data class WeeklyPlanModel(
    val id: String = "",
    val userId: String = "",
    val exerciseId: String = "",
    val exerciseName: String = "",
    val imageUrl: String? = null,
    val day: String = "",
    val reps: Int = 0,
    val sets: Int = 0,
    val notes: String = "",
    var completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
