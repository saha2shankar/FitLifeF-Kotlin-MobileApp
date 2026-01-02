package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import np.com.harishankarsah.fitlife.model.ExerciseModel
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialogState
import np.com.harishankarsah.fitlife.ui.theme.Background
import np.com.harishankarsah.fitlife.ui.theme.Info
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.OnSecondary
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.utils.Size
import np.com.harishankarsah.fitlife.utils.SmsDelegationHelper

enum class ShareDelegationType(val title: String, val icon: ImageVector) {
    EQUIPMENT("Equipment", Icons.Default.CheckBox),
    CHECKLIST("Checklist", Icons.Default.List),
    FULL_PLAN("Full Plan", Icons.Default.Schedule,)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDelegationBottomSheet(
    exercise: ExerciseModel,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf(ShareDelegationType.EQUIPMENT) }
    var phoneNumber by remember { mutableStateOf("") }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var previewMessage by remember { mutableStateOf("") }

    // Mode 1: Equipment State
    // Using a map to track selection state and quantities
    // Key: Index of equipment in list
    val equipmentSelection = remember { mutableStateMapOf<Int, Boolean>() }
    val equipmentQuantities = remember { mutableStateMapOf<Int, String>() }

    // Initialize equipment state
    LaunchedEffect(exercise) {
        exercise.equipment.forEachIndexed { index, _ ->
            equipmentSelection[index] = true // Default select all
            equipmentQuantities[index] = "1"
        }
    }

    // Mode 2: Checklist State
    var reps by remember { mutableStateOf("10") }
    var sets by remember { mutableStateOf("3") }
    var notes by remember { mutableStateOf("") }

    // Mode 3: Full Plan State
    // (Uses exercise data directly)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Background,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Share Workout via SMS",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Mode Selection Tabs
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ShareDelegationType.values().forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = selectedMode == type,
                        onClick = { selectedMode = type },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = ShareDelegationType.values().size),
                        icon = { Icon(imageVector = type.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        label = { Text(type.title) },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Primary,
                            activeContentColor = Color.White,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic Content Area
            when (selectedMode) {
                ShareDelegationType.EQUIPMENT -> {
                    Text("Select equipment to delegate:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (exercise.equipment.isEmpty()) {
                        Text("No equipment listed for this workout.", color = MaterialTheme.colorScheme.error)
                    } else {
                        exercise.equipment.forEachIndexed { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = equipmentSelection[index] == true,
                                    onCheckedChange = { isChecked ->
                                        equipmentSelection[index] = isChecked
                                    }
                                )
                                Text(
                                    text = item,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                if (equipmentSelection[index] == true) {
                                    OutlinedTextField(
                                        value = equipmentQuantities[index] ?: "",
                                        onValueChange = { equipmentQuantities[index] = it },
                                        label = { Text("Qty") },
                                        modifier = Modifier.width(80.dp),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }
                    }
                }
                ShareDelegationType.CHECKLIST -> {
                    Text("Configure Checklist Details:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            readOnly = true,
                            value = sets,
                            onValueChange = { sets = it },
                            label = { Text("Sets") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            readOnly = true,
                            value = reps,
                            onValueChange = { reps = it },
                            label = { Text("Reps") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Exercises Included:", style = MaterialTheme.typography.titleSmall)
                    exercise.exercises.forEach { 
                        Text("• $it", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                    }
                }
                ShareDelegationType.FULL_PLAN -> {
                    Text("Full Plan Preview:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = OnAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Workout: ${exercise.routineName}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${exercise.exercises.size} Exercises")
                            Text("${exercise.equipment.size} Equipment Items")
                            if (exercise.latitude != null && exercise.longitude != null) {
                                Text("Includes Location Data", color = Primary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                label = { Text("Recipient phone") },
                placeholder = { Text("e.g. +977 981 234 5678") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = Primary,
                    cursorColor = Primary,
                    focusedLeadingIconColor = Primary
                )
            )


            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GlobalButton(
                    onClick = {
                        // Generate Message Logic
                        previewMessage = when (selectedMode) {
                            ShareDelegationType.EQUIPMENT -> {
                                val selectedIndices = equipmentSelection.filter { it.value }.map { it.key }
                                SmsDelegationHelper.formatEquipmentMessage(
                                    workoutName = exercise.routineName,
                                    equipmentList = exercise.equipment,
                                    selectedIndices = selectedIndices,
                                    quantities = equipmentQuantities,
                                    lat = exercise.latitude,
                                    lng = exercise.longitude
                                )
                            }
                            ShareDelegationType.CHECKLIST -> {
                                SmsDelegationHelper.formatExerciseChecklistMessage(
                                    workoutName = exercise.routineName,
                                    exerciseList = exercise.exercises,
                                    reps = reps,
                                    sets = sets,
                                    notes = notes,
                                    lat = exercise.latitude,
                                    lng = exercise.longitude
                                )
                            }
                            ShareDelegationType.FULL_PLAN -> {
                                SmsDelegationHelper.formatFullPlanMessage(exercise)
                            }
                        }
                        showPreviewDialog = true
                    },
                    text = "Preview & Send"
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Confirmation Dialog
    if (showPreviewDialog) {
        AlertDialog(
            containerColor = OnAccent,
            onDismissRequest = { showPreviewDialog = false },
            title = { Text("SMS Preview",color = Primary) },
            text = {
                Column {
                    Text("Review the message content before sending:", style = MaterialTheme.typography.bodySmall, color = OnPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .background(Background, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = previewMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSecondary,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPreviewDialog = false
                        onDismissRequest() // Close sheet
                        val success = SmsDelegationHelper.openSmsApp(context, phoneNumber, previewMessage)
                        if (success) {
                             GlobalDialogState.showSuccess("SMS app opened successfully")
                        }
                    }
                ) {
                    Text("Send SMS")
                }
            },
            dismissButton = {

                GlobalTextButton(
                    onClick = { showPreviewDialog = false },
                    text = "Cancel",
                    buttonType=TextButtonType.SECONDARY                )
            }
        )
    }
}
