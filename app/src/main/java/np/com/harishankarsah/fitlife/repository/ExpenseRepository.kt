package np.com.harishankarsah.fitlife.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import np.com.harishankarsah.fitlife.model.ExpenseModel

class ExpenseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("expenses")

    fun getExpensesFlow(): Flow<List<ExpenseModel>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }

        // Remove orderBy to avoid index requirement - will sort in ViewModel
        val listener = collection.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val expenses = snapshot.toObjects(ExpenseModel::class.java)
                    // Sort by date descending (newest first)
                    val sortedExpenses = expenses.sortedByDescending { it.date }
                    trySend(sortedExpenses)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addExpense(expense: ExpenseModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val documentRef = collection.document()
            val newExpense = expense.copy(id = documentRef.id, userId = userId)
            documentRef.set(newExpense).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExpense(expense: ExpenseModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val updatedExpense = expense.copy(userId = userId)
            collection.document(expense.id).set(updatedExpense).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExpense(expenseId: String): Result<Unit> {
        return try {
            collection.document(expenseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExpenseById(expenseId: String): Result<ExpenseModel> {
        return try {
            val snapshot = collection.document(expenseId).get().await()
            val expense = snapshot.toObject(ExpenseModel::class.java)
            if (expense != null) {
                Result.success(expense)
            } else {
                Result.failure(Exception("Expense not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

