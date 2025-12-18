package np.com.harishankarsah.fitlife.ui.components.dialog
sealed class DialogType {
    data class Confirmation(
        val title: String,
        val message: String,
        val confirmText: String = "Yes",
        val cancelText: String = "Cancel",
        val onConfirm: () -> Unit
    ) : DialogType()

    data class Success(val message: String) : DialogType()
    data class Error(val message: String) : DialogType()
    data class Info(val message: String) : DialogType()
    object None : DialogType()
}
