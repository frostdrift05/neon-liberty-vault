package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.AsphaltBlack

data class Glow(
    val color: Color,
    val alpha: Float,
    val fractionX: Float,
    val fractionY: Float,
    val radiusFraction: Float
)

@Composable
fun ScreenBackground(
    photoAlpha: Float,
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    glows: List<Glow> = emptyList(),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsphaltBlack)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(photoAlpha)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
                .drawBehind {
                    glows.forEach { glow ->
                        val radius = size.minDimension * glow.radiusFraction
                        val center = Offset(size.width * glow.fractionX, size.height * glow.fractionY)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(glow.color.copy(alpha = glow.alpha), Color.Transparent),
                                center = center,
                                radius = radius
                            ),
                            radius = radius,
                            center = center
                        )
                    }
                }
        )
        content()
    }
}
