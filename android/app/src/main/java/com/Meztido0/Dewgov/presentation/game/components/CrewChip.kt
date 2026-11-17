package com.Meztido0.Dewgov.presentation.game.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.ui.MeterBar
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.core.ui.jobIconRes
import com.Meztido0.Dewgov.presentation.game.CrewSlot

@Composable
fun CrewChip(
    slot: CrewSlot,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = when {
        !slot.enabled -> BorderDim
        slot.selected -> ChromeCyan
        slot.matchesTarget -> StreetAmber
        else -> ChromeCyan
    }
    val label = if (slot.enabled) {
        stringResource(R.string.state_selected)
    } else {
        stringResource(R.string.state_spent)
    }
    Column(
        modifier = modifier
            .hudSurface(
                borderColor = accent,
                borderAlpha = if (slot.selected) 1f else 0.4f,
                fillAlpha = 0.85f,
                corner = 6.dp,
                bracketArm = 9.dp,
                bracketAlpha = if (slot.selected) 1f else 0.5f,
                bracketStroke = if (slot.selected) 2.dp else 1.5.dp
            )
            .clickable(enabled = slot.enabled) { onSelect(slot.id) }
            .semantics { stateDescription = label }
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .alpha(if (slot.enabled) 1f else 0.4f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(jobIconRes(slot.jobType)),
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(slot.nameRes),
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        MeterBar(
            value = slot.nerve,
            max = 100,
            fill = Brush.horizontalGradient(listOf(ChromeCyan, StreetAmber)),
            barHeight = 4.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = if (slot.enabled) {
                stringResource(R.string.nerve_fmt, slot.nerve)
            } else {
                stringResource(R.string.crew_spent)
            },
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            letterSpacing = 0.6.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}
