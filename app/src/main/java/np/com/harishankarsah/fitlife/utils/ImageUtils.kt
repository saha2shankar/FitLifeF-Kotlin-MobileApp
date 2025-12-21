package np.com.harishankarsah.fitlife.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

/**
 * Saves an image from a Uri to the app's internal storage.
 * @param context The context to access ContentResolver and filesDir.
 * @param uri The Uri of the image to save.
 * @return The absolute path of the saved file, or null if failed.
 */
fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            // Create a directory for images if it doesn't exist
            val imagesDir = File(context.filesDir, "images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }
            
            val fileName = "exercise_${UUID.randomUUID()}.jpg"
            val file = File(imagesDir, fileName)
            
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
            file.absolutePath
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
