package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.NeonMagenta

@Composable
fun EndingDiamondRow(
    endingsSeen: Set<Int>,
    modifier: Modifier = Modifier,
    diamondSize: Dp = 10.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (id in 0 until GameConfig.TOTAL_ENDINGS) {
            Spacer(
                modifier = Modifier
                    .size(diamondSize)
                    .graphicsLayer { rotationZ = 45f }
                    .background(
                        if (endingsSeen.contains(id)) NeonMagenta else BorderDim,
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
