package com.Meztido0.Dewgov.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Int? = null,
    accent: Color = ChromeCyan,
    iconSize: Dp = 20.dp,
    minHeight: Dp = 48.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "secondaryScale"
    )
    val shape = RoundedCornerShape(8.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = BorderStroke(1.dp, if (enabled) accent.copy(alpha = 0.55f) else BorderDim),
        contentPadding = PaddingValues(horizontal = 14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) TextPrimary else TextMuted,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextMuted
        ),
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .height(minHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(SurfaceHud.copy(alpha = if (enabled) 0.72f else 0.4f), shape)
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                tint = if (enabled) accent else TextMuted,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}
