package np.com.harishankarsah.fitlife.ui.screen.dashboard.workplan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.components.GlobalCardVariantsPreview
import np.com.harishankarsah.fitlife.ui.components.GlobalIconButton
import np.com.harishankarsah.fitlife.ui.components.GlobalTextField
import np.com.harishankarsah.fitlife.ui.theme.OnPrimary
import np.com.harishankarsah.fitlife.ui.utils.Size

@Composable
fun WorkOutPlanScreen() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

        ) {
            Text(
                text = "Workout Plan",
                style = MaterialTheme.typography.headlineLarge,
                color = OnPrimary
            )

            GlobalIconButton(
                icon = Icons.Filled.Add,
                contentDescription = "Add Workout",
                onClick = {
                    // Add workout action
                }
            )
        }
        Spacer(modifier = Modifier.height(Size.sm))
        var searchtext by remember { mutableStateOf("") }
        GlobalTextField(
            value = searchtext,
            onValueChange = { searchtext = it },
            placeholder = "Search",
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        Spacer(modifier = Modifier.height(Size.sm))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            GlobalCardVariantsPreview()
        }
    }
}
