package com.Meztido0.Dewgov.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.SplashTop
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceRaised
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.drawCornerBrackets

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
fun SplashScreen() {
    val loop = rememberInfiniteTransition(label = "splashLoop")
    val sweep by loop.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )
    val bracketAlpha by loop.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bracket"
    )
    val emblemPulse by loop.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emblem"
    )

    ScreenBackground(
        photoAlpha = 0.32f,
        overlay = listOf(
            SplashTop.copy(alpha = 0.95f),
            AsphaltBlack.copy(alpha = 0.92f),
            MagentaHaze.copy(alpha = 0.96f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(236.dp)
                    .drawBehind {
                        val radius = size.minDimension * 0.52f
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(NeonMagenta.copy(alpha = 0.3f), Color.Transparent),
                                center = center,
                                radius = radius
                            ),
                            radius = radius,
                            center = center
                        )
                        drawCornerBrackets(
                            ChromeCyan.copy(alpha = bracketAlpha),
                            28.dp.toPx(),
                            2.dp.toPx()
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.splash_emblem_cd),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size((148 * emblemPulse).dp)
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * sweep
                    drawLine(
                        color = ChromeCyan.copy(alpha = 0.55f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = NeonMagenta,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = 3.dp)
                        .alpha(0.45f)
                )
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(38.dp))

            LinearProgressIndicator(
                color = ChromeCyan,
                trackColor = SurfaceRaised,
                modifier = Modifier
                    .width(180.dp)
                    .height(3.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.splash_caption),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(34.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StreetlightDot(phase = sweep, index = 0)
                StreetlightDot(phase = sweep, index = 1)
                StreetlightDot(phase = sweep, index = 2)
            }
        }
    }
}

@Composable
private fun StreetlightDot(phase: Float, index: Int) {
    val shifted = (phase + index * 0.18f) % 1f
    val glow = 0.25f + 0.75f * (1f - kotlin.math.abs(shifted - 0.5f) * 2f)
    Spacer(
        modifier = Modifier
            .size(7.dp)
            .background(StreetAmber.copy(alpha = glow.coerceIn(0.2f, 1f)), CircleShape)
    )
}
