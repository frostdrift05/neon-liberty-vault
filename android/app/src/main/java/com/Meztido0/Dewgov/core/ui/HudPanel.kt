package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.SurfaceHud

fun DrawScope.drawCornerBrackets(color: Color, armPx: Float, strokePx: Float) {
    val w = size.width
    val h = size.height
    val inset = strokePx / 2f
    drawLine(color, Offset(inset, inset), Offset(inset + armPx, inset), strokePx, StrokeCap.Round)
    drawLine(color, Offset(inset, inset), Offset(inset, inset + armPx), strokePx, StrokeCap.Round)
    drawLine(color, Offset(w - inset, inset), Offset(w - inset - armPx, inset), strokePx, StrokeCap.Round)
    drawLine(color, Offset(w - inset, inset), Offset(w - inset, inset + armPx), strokePx, StrokeCap.Round)
    drawLine(color, Offset(inset, h - inset), Offset(inset + armPx, h - inset), strokePx, StrokeCap.Round)
    drawLine(color, Offset(inset, h - inset), Offset(inset, h - inset - armPx), strokePx, StrokeCap.Round)
    drawLine(color, Offset(w - inset, h - inset), Offset(w - inset - armPx, h - inset), strokePx, StrokeCap.Round)
    drawLine(color, Offset(w - inset, h - inset), Offset(w - inset, h - inset - armPx), strokePx, StrokeCap.Round)
}

fun Modifier.hudSurface(
    borderColor: Color,
    borderAlpha: Float = 0.45f,
    fillColor: Color = SurfaceHud,
    fillAlpha: Float = 0.88f,
    corner: Dp = 8.dp,
    bracketArm: Dp = 12.dp,
    bracketAlpha: Float = 0.9f,
    bracketStroke: Dp = 2.dp
): Modifier {
    val shape = RoundedCornerShape(corner)
    return this
        .clip(shape)
        .background(fillColor.copy(alpha = fillAlpha), shape)
        .border(1.dp, borderColor.copy(alpha = borderAlpha), shape)
        .drawWithContent {
            drawContent()
            drawCornerBrackets(
                borderColor.copy(alpha = bracketAlpha),
                bracketArm.toPx(),
                bracketStroke.toPx()
            )
        }
}

@Composable
fun HudPanel(
    modifier: Modifier = Modifier,
    borderColor: Color = ChromeCyan,
    borderAlpha: Float = 0.45f,
    fillColor: Color = SurfaceHud,
    fillAlpha: Float = 0.88f,
    corner: Dp = 8.dp,
    bracketArm: Dp = 12.dp,
    bracketAlpha: Float = 0.9f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.hudSurface(
            borderColor = borderColor,
            borderAlpha = borderAlpha,
            fillColor = fillColor,
            fillAlpha = fillAlpha,
            corner = corner,
            bracketArm = bracketArm,
            bracketAlpha = bracketAlpha
        ),
        content = content
    )
}
