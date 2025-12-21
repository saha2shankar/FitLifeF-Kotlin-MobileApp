package np.com.harishankarsah.fitlife.model

data class ExerciseModel(
    val id: String = "",
    val userId: String = "",
    val routineName: String = "",
    val instructions: String = "",
    val exercises: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)
