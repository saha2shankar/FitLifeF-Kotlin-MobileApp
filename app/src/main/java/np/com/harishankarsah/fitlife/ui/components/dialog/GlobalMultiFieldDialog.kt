package np.com.harishankarsah.fitlife.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.theme.Accent
import np.com.harishankarsah.fitlife.ui.theme.Error
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnSurface
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Shapes
import np.com.harishankarsah.fitlife.ui.theme.Surface

@Composable
fun GlobalMultiFieldDialog() {
    val dialogState = GlobalDialogState.dialog.value
    if (dialogState !is DialogType.MultiField) return

    val fieldStates = remember {
        mutableStateMapOf<String, Any?>().apply {
            dialogState.fields.forEach { field ->
                put(field.label, field.initialValue ?: when (field.type) {
                    FieldType.Text, FieldType.Number -> ""
                    FieldType.Checkbox -> false
                    is FieldType.Dropdown -> field.type.options.firstOrNull() ?: ""
                })
            }
        }
    }

    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        containerColor = Surface,
        onDismissRequest = { GlobalDialogState.dismiss() },
        title = { Text(dialogState.title, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                dialogState.fields.forEach { field ->
                    Spacer(modifier = Modifier.height(8.dp))
                    when (field.type) {
                        FieldType.Text -> {
                            TextField(
                                value = fieldStates[field.label] as String,
                                onValueChange = { fieldStates[field.label] = it },
                                placeholder = { Text(field.placeholder) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = OnAccent,
                                    unfocusedContainerColor = OnAccent,
                                    disabledContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    cursorColor = Primary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                    errorLabelColor = Color.Transparent,
                                    errorPlaceholderColor = Color.Transparent,
                                    errorSupportingTextColor = Color.Transparent,
                                    errorCursorColor = Error,
                                ),
                                shape = Shapes.small,
                            )
                        }
                        FieldType.Number -> {
                            TextField(
                                value = fieldStates[field.label] as String,
                                onValueChange = { fieldStates[field.label] = it.filter { ch -> ch.isDigit() } },
                                placeholder = { Text(field.placeholder) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = OnAccent,
                                unfocusedContainerColor = OnAccent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                cursorColor = Primary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                errorLabelColor = Color.Transparent,
                                errorPlaceholderColor = Color.Transparent,
                                errorSupportingTextColor = Color.Transparent,
                                errorCursorColor = Error,
                            ),
                                shape = Shapes.small,
                            )
                        }
                        FieldType.Checkbox -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = fieldStates[field.label] as Boolean,
                                    onCheckedChange = { fieldStates[field.label] = it }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(field.label)
                            }
                        }
                        is FieldType.Dropdown -> {
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                TextField(
                                    value = fieldStates[field.label] as String,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text(field.placeholder) },
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                    } ,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = OnAccent,
                                        unfocusedContainerColor = OnAccent,
                                        disabledContainerColor = Color.Transparent,
                                        errorContainerColor = Color.Transparent,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        cursorColor = Primary,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        errorIndicatorColor = Color.Transparent,
                                        errorLabelColor = Color.Transparent,
                                        errorPlaceholderColor = Color.Transparent,
                                        errorSupportingTextColor = Color.Transparent,
                                        errorCursorColor = Error,
                                    ),
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { expanded = true }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    field.type.options.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(
                                                            if (fieldStates[field.label] == option)
                                                                Primary
                                                            else
                                                                Surface
                                                        )
                                                        .padding(12.dp)
                                                ) {
                                                    Text(option, color = OnSurface)
                                                }
                                            },
                                            onClick = {
                                                fieldStates[field.label] = option
                                                expanded = false
                                            }
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            GlobalTextButton(
                text = dialogState.confirmText,
                onClick = {
                    GlobalDialogState.dismiss()
                    dialogState.onConfirm(fieldStates.toMap())
                }
            )
        },
        dismissButton = {

            GlobalTextButton(
                text = dialogState.cancelText,
                buttonType = TextButtonType.SECONDARY,
                onClick = { GlobalDialogState.dismiss() }
            )
        }
    )
}
