package np.com.harishankarsah.fitlife.ui.components.dialog

import androidx.compose.runtime.mutableStateOf

object GlobalDialogState {
    val dialog = mutableStateOf<DialogType>(DialogType.None)


    fun dismiss() {
        dialog.value = DialogType.None
    }

    fun showConfirmation(
        title: String,
        message: String,
        confirmText: String = "Yes",
        cancelText: String = "Cancel",
        onConfirm: () -> Unit
    ) {
        dialog.value = DialogType.Confirmation(
            title, message, confirmText, cancelText, onConfirm
        )
    }

    fun showSuccess(msg: String) {
        dialog.value = DialogType.Success(msg)
    }

    fun showError(msg: String) {
        dialog.value = DialogType.Error(msg)
    }

    fun showInfo(msg: String) {
        dialog.value = DialogType.Info(msg)
    }

    fun showMultiField(
        title: String,
        fields: List<DialogField>,
        confirmText: String = "Submit",
        cancelText: String = "Cancel",
        onConfirm: (Map<String, Any?>) -> Unit
    ) {
        dialog.value = DialogType.MultiField(
            title, fields, confirmText, cancelText, onConfirm
        )
    }
}
