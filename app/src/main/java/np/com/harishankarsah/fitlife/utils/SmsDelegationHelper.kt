package np.com.harishankarsah.fitlife.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.model.WeeklyPlanModel

/**
 * Utility class for SMS delegation using implicit intents.
 * This approach does not require SMS permissions as it opens the default SMS app.
 */
object SmsDelegationHelper {

    /**
     * Appends Google Maps location link if coordinates are valid
     */
    private fun appendLocationLink(sb: StringBuilder, lat: Double?, lng: Double?) {
        if (lat != null && lng != null && lat != 0.0 && lng != 0.0) {
            sb.appendLine()
            sb.appendLine("📍 Location:")
            sb.appendLine("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
        }
    }

    /**
     * Formats equipment list into SMS message
     * Mode 1: Equipment List sharing
     */
    fun formatEquipmentMessage(
        workoutName: String,
        equipmentList: List<String>,
        selectedIndices: List<Int>,
        quantities: Map<Int, String>,
        lat: Double? = null,
        lng: Double? = null
    ): String {
        val selectedEquipment = if (selectedIndices.isEmpty()) {
            emptyList() // If nothing selected, send nothing? Or all? Usually explicit selection implies filtering.
        } else {
            selectedIndices.mapNotNull { index ->
                if (index in equipmentList.indices) {
                    val item = equipmentList[index]
                    val qty = quantities[index]?.takeIf { it.isNotBlank() } ?: "1"
                    "- $item (Qty: $qty)"
                } else null
            }
        }

        return buildString {
            appendLine("🏋️ Equipment List for $workoutName")
            appendLine("--------------------------------")
            if (selectedEquipment.isEmpty()) {
                appendLine("(No equipment selected)")
            } else {
                selectedEquipment.forEach { appendLine(it) }
            }
            appendLocationLink(this, lat, lng)
        }
    }

    /**
     * Formats exercise checklist into SMS message
     * Mode 2: Exercise Checklist sharing
     */
    fun formatExerciseChecklistMessage(
        workoutName: String,
        exerciseList: List<String>,
        reps: String,
        sets: String,
        notes: String,
        lat: Double? = null,
        lng: Double? = null
    ): String {
        return buildString {
            appendLine("📝 Exercise Checklist: $workoutName")
            appendLine("--------------------------------")
            appendLine("Target: $sets Sets x $reps Reps")
            if (notes.isNotBlank()) {
                appendLine("Notes: $notes")
            }
            appendLine()
            appendLine("Exercises:")
            exerciseList.forEachIndexed { index, exercise ->
                appendLine("${index + 1}. [ ] $exercise")
            }
            appendLocationLink(this, lat, lng)
        }
    }

    /**
     * Formats full plan message
     * Mode 3: Full Plan sharing
     */
    fun formatFullPlanMessage(
        exercise: ExerciseModel,
        scheduleDay: String = "Any Day"
    ): String {
        return buildString {
            appendLine("📅 Full Workout Plan: ${exercise.routineName}")
            appendLine("--------------------------------")
            appendLine("Suggested Schedule: $scheduleDay")
            
            if (exercise.instructions.isNotBlank()) {
                appendLine()
                appendLine("📄 Instructions:")
                appendLine(exercise.instructions)
            }

            if (exercise.equipment.isNotEmpty()) {
                appendLine()
                appendLine("🎒 Equipment Needed:")
                exercise.equipment.forEach { appendLine("- $it") }
            }

            if (exercise.exercises.isNotEmpty()) {
                appendLine()
                appendLine("💪 Exercises:")
                exercise.exercises.forEachIndexed { index, item ->
                    appendLine("${index + 1}. $item")
                }
            }
            
            appendLocationLink(this, exercise.latitude, exercise.longitude)
        }
    }

    /**
     * Opens SMS app with pre-filled message using implicit intent
     * @param context The context to start the intent
     * @param phoneNumber The recipient phone number (can be empty)
     * @param message The message body
     * @return true if intent was launched successfully, false otherwise
     */
    fun openSmsApp(
        context: Context,
        phoneNumber: String,
        message: String
    ): Boolean {
        return try {
            val uri = if (phoneNumber.isNotBlank()) {
                Uri.parse("smsto:$phoneNumber")
            } else {
                Uri.parse("smsto:")
            }
            
            val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
                putExtra("sms_body", message)
            }
            
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                true
            } else {
                // Fallback for some devices/simulators where resolveActivity might fail but startActivity works
                try {
                    context.startActivity(intent)
                    true
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "No SMS app available",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Failed to open SMS app: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}

