package np.com.harishankarsah.fitlife.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalMultiInputField(
    title: String,
    items: List<String>,
    onItemsChange: (List<String>) -> Unit,
    placeholder: String
) {
    var currentInput by remember { mutableStateOf("") }

    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentInput,
                    onValueChange = { currentInput = it },
                    placeholder = {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .clip(Shapes.small).background(Surface)
                )


                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (currentInput.isNotBlank()) {
                            onItemsChange(items + currentInput.trim())
                            currentInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(Shapes.small)
                        .background(Primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = OnAccent
                    )
                }
            }


            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(Shapes.small)
                    .background(Primary)
            ) {
            IconButton(
                onClick = {
                    if (currentInput.isNotBlank()) {
                        onItemsChange(items + currentInput.trim())
                        currentInput = ""
                    }
                },
                        modifier = Modifier
                        .size(56.dp)
                    .clip(Shapes.small)
                    .background(Primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = OnAccent
                )
            }
        }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            maxItemsInEachRow = 4 // optional: control wrap
        ) {
            items.forEach { item ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = item,
                            color = OnAccent
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    onItemsChange(items - item)
                                }
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Primary
                    ),

                )
            }
        }

    }
}
