package np.com.harishankarsah.fitlife.ui.screen.dashboard.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.model.ExpenseModel
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.components.GlobalTextButton
import np.com.harishankarsah.fitlife.ui.components.TextButtonType
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.theme.OnSurface
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Surface
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExpenseDialog(
    onDismiss: () -> Unit,
    onCreateExpense: (ExpenseModel) -> Unit
) {
    val expenseCategories = listOf(
        "Gym Membership",
        "Personal Training",
        "Workout Equipment",
        "Fitness Classes",
        "Supplements & Nutrition",
        "Sports Apparel",
        "Health Checkups",
        "Fitness Apps",
        "Other"
    )


    var selectedCategory by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    
    var categoryError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(selectedDate))

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = Surface,
        title = {
            Text(
                text = "Add New Expense",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category Dropdown using ExposedDropdownMenuBox
                Column {
                    Text(
                        text = "Category *",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = showCategoryDropdown,
                        onExpandedChange = { showCategoryDropdown = !showCategoryDropdown }
                    ) {
                        GlobalTextField(
                            value = selectedCategory,
                            onValueChange = { },
                            placeholder = "Select category",
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = showCategoryDropdown
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            error = categoryError
                        )
                        ExposedDropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            expenseCategories.forEach { category ->
                                val isSelected = selectedCategory == category

                                DropdownMenuItem(
                                    onClick = {
                                        selectedCategory = category
                                        showCategoryDropdown = false
                                        categoryError = null
                                    },
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (isSelected) Primary else Surface
                                                )
                                                .padding(15.dp)
                                        ) {
                                            Text(
                                                text = category,
                                                color = if (isSelected) OnPrimary else OnSurface
                                            )
                                        }
                                    },
                                    contentPadding = PaddingValues(0.dp) // 🔥 VERY IMPORTANT
                                )
                            }
                        }

                    }
                }

                // Amount
                GlobalTextField(
                    value = amountText,
                    onValueChange = { newValue ->
                        // Allow only numbers and one decimal point
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amountText = newValue
                            amountError = null
                        }
                    },
                    label = "Amount *",
                    placeholder = "0.00",
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    leadingIcon = {
                        Text(
                            text = "NRP",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                        )
                    },
                    error = amountError
                )

                // Description
                GlobalTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description",
                    placeholder = "Optional description",
                    singleLine = false,
                    maxLines = 3,
                    minLines = 2
                )

                // Date Display (auto-selected as today, editable via timestamp)
                Column {
                    Text(
                        text = "Date *",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    GlobalTextField(
                        value = dateString,
                        onValueChange = { },
                        placeholder = "Date",
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Date",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Text(
                        text = "Date is automatically set to today. You can modify it in the future.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            GlobalTextButton(
                text = "Add Expense",
                onClick = {
                    // Validation
                    var isValid = true
                    
                    if (selectedCategory.isEmpty()) {
                        categoryError = "Please select a category"
                        isValid = false
                    }
                    
                    if (amountText.isEmpty() || amountText.toDoubleOrNull() == null || amountText.toDouble() <= 0) {
                        amountError = "Please enter a valid amount"
                        isValid = false
                    }
                    
                    if (isValid) {
                        val expense = ExpenseModel(
                            category = selectedCategory,
                            amount = amountText.toDouble(),
                            description = description.trim(),
                            date = selectedDate
                        )
                        onCreateExpense(expense)
                    }
                },
                buttonType = TextButtonType.PRIMARY,
            )
        },
        dismissButton = {

            GlobalTextButton(
                onClick = onDismiss,
                text = "Cancel",
                buttonType = TextButtonType.SECONDARY
            )

        }
    )
}

