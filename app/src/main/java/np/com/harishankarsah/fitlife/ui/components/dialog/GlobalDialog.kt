package np.com.harishankarsah.fitlife.ui.components.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalDialog() {
    val dialogState = GlobalDialogState.dialog.value

    if (dialogState is DialogType.None) return

    AlertDialog(
        onDismissRequest = { GlobalDialogState.dismiss() },
        modifier = Modifier,
        shape = MaterialTheme.shapes.medium,
        containerColor = Surface,

        // ---------- TITLE WITH ICON ----------
        title = {
            Row {
                when (dialogState) {
                    // Success dialog
                    is DialogType.Success ->
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Success
                        )

                    // Error dialog
                    is DialogType.Error ->
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Error
                        )

                    // Info dialog
                    is DialogType.Info ->
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Info
                        )

                    else -> {}
                }

                if (dialogState !is DialogType.Confirmation) {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                when (dialogState) {
                    is DialogType.Confirmation ->
                        Text(dialogState.title, color = OnSurface, style = MaterialTheme.typography.titleLarge)

                    is DialogType.Success ->
                        Text("Success", color = Success, style = MaterialTheme.typography.titleLarge)

                    is DialogType.Error ->
                        Text("Something went wrong", color = Error, style = MaterialTheme.typography.titleLarge)

                    is DialogType.Info ->
                        Text("Info", color = Info, style = MaterialTheme.typography.titleLarge)

                    else -> {}
                }
            }
        },

        // ---------- MESSAGE ----------
        text = {
            when (dialogState) {
                is DialogType.Confirmation ->
                    Text(dialogState.message, color = OnSurface, style = MaterialTheme.typography.bodyMedium)

                is DialogType.Success ->
                    Text(dialogState.message, color = OnSuccess, style = MaterialTheme.typography.bodyMedium)

                is DialogType.Error ->
                    Text(dialogState.message, color = OnError, style = MaterialTheme.typography.bodyMedium)

                is DialogType.Info ->
                    Text(dialogState.message, color = OnInfo, style = MaterialTheme.typography.bodyMedium)

                else -> {}
            }
        },

        // ---------- CONFIRM BUTTON ----------
        confirmButton = {
            when (dialogState) {
                is DialogType.Confirmation -> {
                    GlobalTextButton(
                        text = dialogState.confirmText,
                        buttonType = TextButtonType.PRIMARY,
                        onClick = {
                            GlobalDialogState.dismiss()
                            dialogState.onConfirm()
                        }
                    )
                }
                else -> {
                    GlobalTextButton(
                        text = "OK",
                        onClick = { GlobalDialogState.dismiss() }
                    )
                }
            }
        },

        // ---------- DISMISS BUTTON ----------
        dismissButton = {
            if (dialogState is DialogType.Confirmation) {
                GlobalTextButton(
                    text = dialogState.cancelText,
                    buttonType = TextButtonType.SECONDARY,
                    onClick = { GlobalDialogState.dismiss() }
                )
            }
        }
    )
}
