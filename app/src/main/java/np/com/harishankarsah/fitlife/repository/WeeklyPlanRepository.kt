package np.com.harishankarsah.fitlife.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WeeklyPlanRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("weekly_plans")

    fun getWeeklyPlansFlow(): Flow<List<WeeklyPlanModel>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }
        
        val listener = collection.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val plans = snapshot.toObjects(WeeklyPlanModel::class.java)
                    trySend(plans)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addWeeklyPlan(weeklyPlan: WeeklyPlanModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val documentRef = collection.document()
            val newPlan = weeklyPlan.copy(id = documentRef.id, userId = userId)
            documentRef.set(newPlan).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeeklyPlans(): Result<List<WeeklyPlanModel>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val snapshot = collection.whereEqualTo("userId", userId).get().await()
            val plans = snapshot.toObjects(WeeklyPlanModel::class.java)
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateWeeklyPlan(weeklyPlan: WeeklyPlanModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val updatedPlan = weeklyPlan.copy(userId = userId)
            collection.document(weeklyPlan.id).set(updatedPlan).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteWeeklyPlan(planId: String): Result<Unit> {
        return try {
            collection.document(planId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeeklyPlanById(planId: String): Result<WeeklyPlanModel> {
        return try {
            val snapshot = collection.document(planId).get().await()
            val plan = snapshot.toObject(WeeklyPlanModel::class.java)
            if (plan != null) {
                Result.success(plan)
            } else {
                Result.failure(Exception("Plan not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getWeeklyPlansCountFlow(): Flow<Int> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }
        
        val listener = collection.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.size())
                }
            }
        awaitClose { listener.remove() }
    }
    
    fun getCompletedWeeklyPlansCountFlow(): Flow<Int> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }
        
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("completed", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.size())
                }
            }
        awaitClose { listener.remove() }
    }
}
