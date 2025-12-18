package np.com.harishankarsah.fitlife.viewmodel

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogs(
    openDialog: MutableState<Boolean>,
    title: String = "Alert Dialog!",
    text: String
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(text = text) },
            confirmButton = {
                Button(
                    onClick = { openDialog.value = false },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Text("OK")
                }
            }
            // Optional: you can add dismissButton = { ... } if needed
        )
    }
}
