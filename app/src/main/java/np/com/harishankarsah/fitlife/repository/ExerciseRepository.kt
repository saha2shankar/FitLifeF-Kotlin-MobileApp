package np.com.harishankarsah.fitlife.repository

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import np.com.harishankarsah.fitlife.model.ExerciseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ExerciseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("exercises")

    fun getExercisesFlow(): Flow<List<ExerciseModel>> = callbackFlow {
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
                    val exercises = snapshot.toObjects(ExerciseModel::class.java)
                    trySend(exercises)
                }
            }
            
        awaitClose { listener.remove() }
    }

    fun getExerciseFlow(exerciseId: String): Flow<ExerciseModel?> = callbackFlow {
        val listener = collection.document(exerciseId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    val exercise = snapshot.toObject(ExerciseModel::class.java)
                    trySend(exercise)
                } else {
                    trySend(null)
                }
            }
            
        awaitClose { listener.remove() }
    }

    suspend fun addExercise(exercise: ExerciseModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val documentRef = collection.document()
            val newExercise = exercise.copy(id = documentRef.id, userId = userId)
            documentRef.set(newExercise).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExercises(): Result<List<ExerciseModel>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val snapshot = collection.whereEqualTo("userId", userId).get().await()
            val exercises = snapshot.toObjects(ExerciseModel::class.java)
            Result.success(exercises)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExercise(exercise: ExerciseModel): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val updatedExercise = exercise.copy(userId = userId)
            collection.document(exercise.id).set(updatedExercise).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExercise(exerciseId: String): Result<Unit> {
        return try {
            collection.document(exerciseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExerciseById(exerciseId: String): Result<ExerciseModel> {
        return try {
            val snapshot = collection.document(exerciseId).get().await()
            val exercise = snapshot.toObject(ExerciseModel::class.java)
            if (exercise != null) {
                Result.success(exercise)
            } else {
                Result.failure(Exception("Exercise not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getExercisesCountFlow(): Flow<Int> = callbackFlow {
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
}
