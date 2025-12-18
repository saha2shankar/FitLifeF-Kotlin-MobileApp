package np.com.harishankarsah.fitlife

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.ui.screen.login.LoginActivity
import np.com.harishankarsah.fitlife.ui.theme.Info
import np.com.harishankarsah.fitlife.ui.theme.OnAccent
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary
import np.com.harishankarsah.fitlife.ui.theme.Typography

@Composable
fun SplashScreen() {

    val context = LocalContext.current
    val activity = context as? Activity

    // Multiple animations for professional sequencing
    val lineProgress = remember { Animatable(0f) }
    val circleScale = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val gradientRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Sequence animations professionally
        val lineAnimation = async {
            lineProgress.animateTo(
                1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            )
        }

        lineAnimation.await()

        val circleAnimation = async {
            circleScale.animateTo(
                1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }

        val logoAnimation = async {
            logoAlpha.animateTo(
                1f,
                animationSpec = tween(400)
            )
            logoScale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 300f
                )
            )
        }

        circleAnimation.await()
        logoAnimation.await()

        // Continuous gradient rotation
        launch {
            while (true) {
                gradientRotation.animateTo(
                    gradientRotation.value + 360f,
                    animationSpec = tween(
                        durationMillis = 20000,
                        easing = LinearEasing
                    )
                )
            }
        }

        delay(300)

        textAlpha.animateTo(
            1f,
            animationSpec = tween(800)
        )

        delay(1000)

         activity?.startActivity(Intent(context, LoginActivity::class.java))
         activity?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnAccent)
    ) {

        // ===== ANIMATED GRADIENT BACKGROUND =====
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Create a shimmering gradient
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Primary.copy(alpha = 0.05f),
                    Secondary.copy(alpha = 0.03f),
                    Info.copy(alpha = 0.05f)
                ),
                start = Offset(0f, 0f),
                end = Offset(canvasWidth, canvasHeight)
            )

            rotate(gradientRotation.value) {
                drawCircle(
                    brush = gradient,
                    radius = canvasWidth * 0.8f,
                    center = Offset(center.x, center.y)
                )
            }
        }

        // ===== ANIMATED CIRCLE ELEMENTS =====
        Canvas(modifier = Modifier.fillMaxSize()) {
            val progress = circleScale.value

            // Outer circle
            drawCircle(
                color = Secondary.copy(alpha = 0.15f),
                radius = size.minDimension * 0.35f * progress,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner circle
            drawCircle(
                color = Primary.copy(alpha = 0.1f),
                radius = size.minDimension * 0.2f * progress,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // ===== GEOMETRIC LINES WITH ENHANCED ANIMATION =====
        Canvas(modifier = Modifier.fillMaxSize()) {

            val progress = lineProgress.value

            // Top-right diagonal line
            val topLineStart = Offset(
                x = size.width * 0.15f,
                y = size.height * 0.2f
            )

            val topLineEnd = Offset(
                x = size.width * (0.15f + 0.3f * progress),
                y = size.height * (0.2f - 0.1f * progress)
            )

            drawLine(
                color = Primary,
                start = topLineStart,
                end = topLineEnd,
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
            )

            // Bottom-left diagonal line
            val bottomLineStart = Offset(
                x = size.width * 0.85f,
                y = size.height * 0.8f
            )

            val bottomLineEnd = Offset(
                x = size.width * (0.85f - 0.3f * progress),
                y = size.height * (0.8f + 0.1f * progress)
            )

            drawLine(
                color = Secondary,
                start = bottomLineStart,
                end = bottomLineEnd,
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
            )

            // Horizontal accent lines
            val leftLineY = size.height * 0.4f
            drawLine(
                color = Primary.copy(alpha = 0.6f),
                start = Offset(x = 0f, y = leftLineY),
                end = Offset(x = size.width * 0.2f * progress, y = leftLineY),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            val rightLineY = size.height * 0.6f
            drawLine(
                color = Secondary.copy(alpha = 0.6f),
                start = Offset(x = size.width, y = rightLineY),
                end = Offset(x = size.width * (1 - 0.2f * progress), y = rightLineY),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // ===== MAIN CONTENT =====
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with scale animation
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "FitLife Logo",
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    MaterialTheme {
        SplashScreen()
    }
}