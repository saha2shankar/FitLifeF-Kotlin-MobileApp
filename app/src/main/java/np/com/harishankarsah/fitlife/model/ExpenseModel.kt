package np.com.harishankarsah.fitlife.model

data class ExpenseModel(
    val id: String = "",
    val userId: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

