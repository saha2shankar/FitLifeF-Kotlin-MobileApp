package np.com.harishankarsah.fitlife

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import np.com.harishankarsah.fitlife.ui.screen.login.LoginActivity
import np.com.harishankarsah.fitlife.ui.theme.Primary
import np.com.harishankarsah.fitlife.ui.theme.Secondary


@Composable
fun SplashScreen() {
    val context = LocalContext.current
    val animationProgress = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }



    // Animate lines
    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(3000, easing = FastOutSlowInEasing)
        )


        // Navigate to LoginActivity after animation
        delay(1000)
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 Top-left animated line (60°)
        Canvas(
            modifier = Modifier
                .size(200.dp, 200.dp)
                .padding(top = 100.dp)
                .align(Alignment.TopStart)
        ) {
            val progress = animationProgress.value
            drawLine(
                color = Primary,
                start = Offset(0f, 0f),
                end = Offset(size.width * progress, size.height * progress),
                strokeWidth = 5.dp.toPx()
            )
        }

        Canvas(
            modifier = Modifier
                .size(200.dp, 200.dp)
                .padding(bottom = 100.dp)
                .align(Alignment.BottomEnd)
        ) {
            val progress = animationProgress.value
            val reverseProgress = 1f - progress

            drawLine(
                color = Secondary,
                start = Offset(size.width, size.height), // start from bottom-right
                end = Offset(size.width * reverseProgress, size.height * reverseProgress),
                strokeWidth = 5.dp.toPx()
            )
        }



        // 🔹 Center Logo
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(alpha = logoAlpha.value)
                .align(Alignment.Center)
        )
    }
}
