package com.Meztido0.Dewgov.presentation.game.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.PatrolRed
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.domain.model.JobOutcome
import com.Meztido0.Dewgov.presentation.game.ResolveUi

@Composable
fun ResolveCard(
    resolve: ResolveUi,
    modifier: Modifier = Modifier
) {
    val accent = when {
        resolve.banking -> StreetAmber
        resolve.outcome == JobOutcome.CLEAN -> ChromeCyan
        resolve.outcome == JobOutcome.CLOSE_CALL -> StreetAmber
        else -> PatrolRed
    }
    val pulse = rememberInfiniteTransition(label = "resolvePulse")
    val breathe by pulse.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 620, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "resolveBreathe"
    )
    val text = when {
        resolve.banking -> stringResource(R.string.resolve_banked_fmt, resolve.amount)
        resolve.outcome == JobOutcome.CLEAN -> stringResource(R.string.resolve_clean_fmt, resolve.amount)
        resolve.outcome == JobOutcome.CLOSE_CALL -> stringResource(R.string.resolve_close_fmt, resolve.amount)
        else -> stringResource(R.string.resolve_burned)
    }
    Row(
        modifier = modifier
            .hudSurface(
                borderColor = accent,
                borderAlpha = breathe,
                fillAlpha = 0.95f,
                corner = 10.dp,
                bracketArm = 14.dp,
                bracketAlpha = breathe
            )
            .padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = accent
        )
    }
}
