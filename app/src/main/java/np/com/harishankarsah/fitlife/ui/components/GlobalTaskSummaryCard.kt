package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalTaskSummaryCard(
    modifier: Modifier = Modifier,
    totalTasks: Int,
    completedTasks: Int,
    remainingTasks: Int,
    title: String = "Task Summary",
    cardColor: Color = MaterialTheme.colorScheme.background,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 16.dp,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(cornerRadius))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            // Task Items Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TaskCountItemEnhanced("Total", totalTasks, Info)

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .height(60.dp) // height of the divider
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                )

                TaskCountItemEnhanced("Completed", completedTasks, Primary)

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                )

                TaskCountItemEnhanced("Remaining", remainingTasks, Secondary)
            }

        }
    }
}

@Composable
private fun TaskCountItemEnhanced(
    label: String,
    count: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular progress indicator for each task type
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(60.dp)
        ) {
            Canvas (modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2
                val stroke = 6.dp.toPx()
                // Background circle
                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    radius = radius - stroke / 2,
                    style = Stroke(width = stroke)
                )
                // Foreground arc (fully filled for simplicity)
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            // Count text in center
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}
