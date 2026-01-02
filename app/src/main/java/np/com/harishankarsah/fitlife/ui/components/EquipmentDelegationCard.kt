package np.com.harishankarsah.fitlife.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.theme.FitLifeTheme
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.utils.SmsDelegationHelper

@Composable
fun EquipmentDelegationCardSMS(
    title: String = "Equipment Delegation",
    equipmentList: List<String>
) {
    val context = LocalContext.current

    // State for checkboxes and quantities
    val checkedStates = remember { equipmentList.map { mutableStateOf(false) } }
    val quantities = remember { equipmentList.map { mutableStateOf("") } }
    var contactNumber by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Equipment List with Checkboxes + Quantity
            equipmentList.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = checkedStates[index].value,
                        onCheckedChange = { checkedStates[index].value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = item,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Quantity Input Only If Checked
                    if (checkedStates[index].value) {
                        GlobalTextField(
                            value = quantities[index].value,
                            onValueChange = { quantities[index].value = it },
                            placeholder = "Qty",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Number Input
            GlobalTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                placeholder = "Enter contact number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(Size.md))

            // Send SMS Button
            GlobalButton(
                onClick = {
                    val sent = sendSmsViaIntent(
                        context,
                        contactNumber,
                        equipmentList,
                        checkedStates,
                        quantities
                    )
                    // ✅ Reset after success
                    if (sent) {
                        checkedStates.forEach { it.value = false }
                        quantities.forEach { it.value = "" }
                        contactNumber = ""
                    }
                },
                text = "Open SMS App"
            )
        }
    }
}

/**
 * Helper function that uses implicit intent to open SMS app
 * No permissions required - opens default SMS app with pre-filled message
 */
private fun sendSmsViaIntent(
    context: Context,
    phoneNumber: String,
    equipmentList: List<String>,
    checkedStates: List<MutableState<Boolean>>,
    quantities: List<MutableState<String>>
): Boolean {
    val selectedIndices = checkedStates.mapIndexedNotNull { index, state ->
        if (state.value) index else null
    }

    if (selectedIndices.isEmpty()) {
        GlobalDialogState.showError("Select at least one equipment item")
        return false
    }

    val quantitiesMap = selectedIndices.associateWith { index ->
        quantities[index].value.ifBlank { "1" }
    }

    val message = SmsDelegationHelper.formatEquipmentMessage(
        workoutName = "Delegated Equipment",
        equipmentList = equipmentList,
        selectedIndices = selectedIndices,
        quantities = quantitiesMap
    )

    val success = SmsDelegationHelper.openSmsApp(context, phoneNumber, message)
    if (success) {
        GlobalDialogState.showSuccess("Opening SMS app...")
    }
    return success
}


@Preview(showBackground = true)
@Composable
fun EquipmentDelegationCardSMSPreview() {
    FitLifeTheme {
        EquipmentDelegationCardSMS(
            title = "Delegate Equipment",
            equipmentList = listOf(
                "Dumbbell",
                "Yoga Mat",
                "Resistance Band",
                "Kettlebell",
                "Jump Rope"
            )
        )
    }
}