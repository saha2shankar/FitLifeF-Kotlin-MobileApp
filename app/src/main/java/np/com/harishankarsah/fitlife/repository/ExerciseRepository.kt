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

    suspend fun seedDefaultExercises(): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            val snapshot = collection.whereEqualTo("userId", userId).limit(1).get().await()
            if (!snapshot.isEmpty) {
                return Result.success(Unit) // Already has exercises
            }

            val batch = db.batch()
            
            val defaults = listOf(
                ExerciseModel(
                    userId = userId,
                    routineName = "Full-Body Strength Training",
                    instructions = "Perform 3 sets of 12 reps for each exercise. Rest 60s between sets.",
                    exercises = listOf("Squats", "Push-ups", "Lunges", "Plank", "Dumbbell Rows"),
                    equipment = listOf("Dumbbells", "Mat"),
                    imageUrl ="https://youfit.com/wp-content/uploads/2023/06/full-body-strength.jpg",
                    createdAt = System.currentTimeMillis()
                ),
                ExerciseModel(
                    userId = userId,
                    routineName = "HIIT Cardio",
                    instructions = "40 seconds work, 20 seconds rest. Repeat circuit 4 times.",
                    exercises = listOf("Jumping Jacks", "Burpees", "Mountain Climbers", "High Knees"),
                    equipment = listOf("None"),
                    imageUrl ="https://images.pexels.com/photos/20901484/pexels-photo-20901484.jpeg?_gl=1*1gee8oi*_ga*MTcxOTI5MTcwNS4xNzY2Nzg3MDU0*_ga_8JE65Q40S6*czE3NjY3ODcwNTMkbzEkZzEkdDE3NjY3ODcwNjEkajUyJGwwJGgw",
                    createdAt = System.currentTimeMillis()
                ),
                ExerciseModel(
                    userId = userId,
                    routineName = "Morning Yoga Flow",
                    instructions = "Hold each pose for 5-10 breaths. Focus on deep breathing.",
                    exercises = listOf("Child's Pose", "Cat-Cow", "Downward Dog", "Warrior I", "Warrior II"),
                    equipment = listOf("Yoga Mat"),
                    imageUrl ="https://theyogahub.ie/wp-content/uploads/2017/11/Yoga-shutterstock_126464135.jpg",
                    createdAt = System.currentTimeMillis()
                )
            )

            defaults.forEach { exercise ->
                val docRef = collection.document()
                batch.set(docRef, exercise.copy(id = docRef.id))
            }

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
