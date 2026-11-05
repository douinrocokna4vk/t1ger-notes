package com.Telqopu0.Kitdag.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.GoldSweep
import com.Telqopu0.Kitdag.core.theme.LacquerFelt
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.PaleGold
import com.Telqopu0.Kitdag.core.theme.SplashOverlay
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.ui.AppBackground

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
) {
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent(modifier = modifier)
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val medallionScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.78f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
    )
    val medallionAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 620)
    )
    val titleOffset by animateFloatAsState(
        targetValue = if (entered) 0f else 24f,
        animationSpec = tween(durationMillis = 520, delayMillis = 240, easing = FastOutSlowInEasing)
    )

    val transition = rememberInfiniteTransition()
    val glowOne by transition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse)
    )
    val glowTwo by transition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.20f,
        animationSpec = infiniteRepeatable(tween(3400), RepeatMode.Reverse)
    )
    val glowThree by transition.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.18f,
        animationSpec = infiniteRepeatable(tween(4200), RepeatMode.Reverse)
    )
    val ringAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Restart)
    )

    AppBackground(overlay = SplashOverlay, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val first = Offset(size.width * 0.22f, size.height * 0.20f)
            val second = Offset(size.width * 0.82f, size.height * 0.34f)
            val third = Offset(size.width * 0.50f, size.height * 0.88f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LuckyGold.copy(alpha = glowOne), Color.Transparent),
                    center = first,
                    radius = size.minDimension * 0.42f
                ),
                radius = size.minDimension * 0.42f,
                center = first
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LuckyGold.copy(alpha = glowTwo), Color.Transparent),
                    center = second,
                    radius = size.minDimension * 0.36f
                ),
                radius = size.minDimension * 0.36f,
                center = second
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LuckyGold.copy(alpha = glowThree), Color.Transparent),
                    center = third,
                    radius = size.minDimension * 0.50f
                ),
                radius = size.minDimension * 0.50f,
                center = third
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(248.dp)
                        .graphicsLayer { rotationZ = ringAngle }
                        .border(
                            border = BorderStroke(2.dp, LuckyGold.copy(alpha = 0.25f)),
                            shape = CircleShape
                        )
                )
                Surface(
                    modifier = Modifier
                        .size(220.dp)
                        .graphicsLayer {
                            scaleX = medallionScale
                            scaleY = medallionScale
                            alpha = medallionAlpha
                        },
                    shape = CircleShape,
                    color = LacquerFelt.copy(alpha = 0.85f),
                    border = BorderStroke(3.dp, Brush.sweepGradient(GoldSweep)),
                    shadowElevation = 18.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = stringResource(R.string.cd_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(176.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(188.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(LuckyGold.copy(alpha = 0.55f))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                color = LuckyGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { translationY = titleOffset }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(188.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(LuckyGold.copy(alpha = 0.55f))
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.brand_subtitle),
                style = MaterialTheme.typography.labelLarge,
                color = PaleGold.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(medallionAlpha)
            )

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(200.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LuckyGold,
                trackColor = LuckyGold.copy(alpha = 0.18f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
