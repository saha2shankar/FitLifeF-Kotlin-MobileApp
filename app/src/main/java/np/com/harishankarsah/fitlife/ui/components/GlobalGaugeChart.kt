package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * A customizable gauge chart component for displaying progress values.
 * Features smooth animations, customizable colors, and professional styling.
 *
 * @param modifier Modifier for the component layout
 * @param value Current value to display (between minValue and maxValue)
 * @param minValue Minimum value of the gauge (default: 0f)
 * @param maxValue Maximum value of the gauge (default: 100f)
 * @param startAngle Starting angle of the gauge arc (default: 150f)
 * @param sweepAngle Total sweep angle of the gauge (default: 240f)
 * @param size Diameter of the gauge circle
 * @param strokeWidth Width of the gauge stroke
 * @param backgroundColor Color for the background track
 * @param progressColor Color for the progress track
 * @param showPercentage Whether to show the percentage text
 * @param showValue Whether to show the actual value text
 * @param showTicks Whether to show tick marks on the gauge
 * @param animationDuration Duration of the progress animation in milliseconds
 * @param animationEasing Easing function for the animation
 * @param textStyle TextStyle for the value text
 * @param label Optional label text to display below the value
 */
@Composable
fun GlobalGaugeChart(
    modifier: Modifier = Modifier,
    value: Float,
    minValue: Float = 0f,
    maxValue: Float = 100f,
    startAngle: Float = 150f,
    sweepAngle: Float = 240f,
    size: Dp = 200.dp,
    strokeWidth: Dp = 20.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    progressColor: Color = MaterialTheme.colorScheme.primary,
    showPercentage: Boolean = true,
    showValue: Boolean = true,
    showTicks: Boolean = true,
    animationDuration: Int = 1000,
    animationEasing: Easing = FastOutSlowInEasing,
    textStyle: TextStyle = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    label: String? = null
) {
    // Calculate normalized progress value
    val normalizedValue = ((value - minValue) / (maxValue - minValue)).coerceIn(0f, 1f)

    // Animated progress value for smooth transitions
    val animatedProgress = remember { Animatable(initialValue = 0f) }

    // Animate the gauge progress
    LaunchedEffect(normalizedValue) {
        animatedProgress.animateTo(
            targetValue = normalizedValue,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = animationEasing
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.toPx()
            val canvasHeight = size.toPx()
            val centerX = canvasWidth / 2
            val centerY = canvasHeight / 2

            // Calculate radius considering stroke width
            val radius = minOf(canvasWidth, canvasHeight) / 2 - strokeWidth.toPx() / 2

            // Draw background track
            drawGaugeArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                strokeWidth = strokeWidth.toPx(),
                radius = radius,
                center = Offset(centerX, centerY),
                drawProgressCap = false
            )

            // Draw progress track with smooth animation
            if (animatedProgress.value > 0) {
                drawGaugeArc(
                    color = progressColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * animatedProgress.value,
                    strokeWidth = strokeWidth.toPx(),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    drawProgressCap = true
                )
            }

            // Draw tick marks if enabled
            if (showTicks) {
                drawTickMarks(
                    tickCount = 11,
                    tickLength = 8.dp.toPx(),
                    tickWidth = 2f,
                    color = progressColor.copy(alpha = 0.5f),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle
                )
            }
        }

        // Value display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            if (showValue) {
                Text(
                    text = if (showPercentage) {
                        "${(normalizedValue * 100).toInt()}%"
                    } else {
                        value.toInt().toString()
                    },
                    style = textStyle.copy(color = progressColor),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

/**
 * Draws a gauge arc with customizable styling
 */
private fun DrawScope.drawGaugeArc(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    strokeWidth: Float,
    radius: Float,
    center: Offset,
    drawProgressCap: Boolean
) {
    // Calculate the rectangle for the arc
    val arcRect = androidx.compose.ui.geometry.Rect(
        left = center.x - radius,
        top = center.y - radius,
        right = center.x + radius,
        bottom = center.y + radius
    )

    // Draw the arc
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = if (drawProgressCap && sweepAngle > 0) StrokeCap.Round else StrokeCap.Butt
        ),
        size = Size(arcRect.width, arcRect.height),
        topLeft = Offset(arcRect.left, arcRect.top)
    )
}

/**
 * Draws tick marks around the gauge
 */
private fun DrawScope.drawTickMarks(
    tickCount: Int,
    tickLength: Float,
    tickWidth: Float,
    color: Color,
    radius: Float,
    center: Offset,
    startAngle: Float,
    sweepAngle: Float
) {
    for (i in 0..tickCount) {
        val angle = startAngle + (sweepAngle / tickCount * i)
        val angleRad = Math.toRadians(angle.toDouble())

        // Calculate tick positions
        val innerRadius = radius - tickLength
        val outerRadius = radius + 2 // Slight offset for better visibility

        val startX = center.x + innerRadius * cos(angleRad).toFloat()
        val startY = center.y + innerRadius * sin(angleRad).toFloat()
        val endX = center.x + outerRadius * cos(angleRad).toFloat()
        val endY = center.y + outerRadius * sin(angleRad).toFloat()

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = tickWidth,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Preview composable for the gauge chart
 */
@Composable
fun GlobalGaugeChartPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Example 2: Customized gauge
        GlobalGaugeChart(
            value = 65f,
            size = 150.dp,
            strokeWidth = 12.dp,
            progressColor = Color(0xFF4CAF50),
            showPercentage = true,
            showValue = true,
            label = "Fitness Score"
        )

        // Example 3: Without percentage
        GlobalGaugeChart(
            value = 120f,
            minValue = 0f,
            maxValue = 200f,
            showPercentage = false,
            label = "Calories Burned"
        )
    }
}