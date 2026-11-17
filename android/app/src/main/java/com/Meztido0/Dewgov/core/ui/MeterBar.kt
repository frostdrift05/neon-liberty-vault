package com.Meztido0.Dewgov.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.SurfaceRaised

@Composable
fun MeterBar(
    value: Int,
    max: Int,
    fill: Brush,
    modifier: Modifier = Modifier,
    barHeight: Dp = 8.dp,
    markerAt: Float = -1f,
    markerColor: Color = Color.Transparent,
    stateLabel: String? = null,
    polite: Boolean = false
) {
    val target = if (max <= 0) 0f else (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 420),
        label = "meterFill"
    )
    val shape = RoundedCornerShape(barHeight / 2)
    val marker = markerAt
    Box(
        modifier = modifier
            .height(barHeight)
            .clip(shape)
            .background(SurfaceRaised)
            .drawWithContent {
                drawContent()
                if (marker in 0f..1f) {
                    val x = size.width * marker
                    drawLine(
                        markerColor,
                        Offset(x, 0f),
                        Offset(x, size.height),
                        2.dp.toPx()
                    )
                }
            }
            .semantics {
                if (stateLabel != null) stateDescription = stateLabel
                if (polite) liveRegion = LiveRegionMode.Polite
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animated.coerceIn(0.004f, 1f))
                .background(fill)
        )
    }
}
