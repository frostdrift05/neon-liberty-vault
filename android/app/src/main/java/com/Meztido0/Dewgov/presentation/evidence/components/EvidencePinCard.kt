package com.Meztido0.Dewgov.presentation.evidence.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.presentation.evidence.PinSlot

@Composable
fun EvidencePinCard(
    slot: PinSlot,
    modifier: Modifier = Modifier
) {
    val accent = if (slot.unlocked) NeonMagenta else ChromeCyan
    val label = if (slot.unlocked) {
        stringResource(R.string.state_visited)
    } else {
        stringResource(R.string.state_locked)
    }
    Column(
        modifier = modifier
            .hudSurface(
                borderColor = if (slot.unlocked) accent else BorderDim,
                borderAlpha = if (slot.unlocked) 0.55f else 0.4f,
                fillAlpha = 0.82f,
                corner = 6.dp,
                bracketArm = 9.dp,
                bracketAlpha = if (slot.unlocked) 0.85f else 0.45f,
                bracketStroke = 1.5.dp
            )
            .semantics { stateDescription = label }
            .padding(horizontal = 6.dp, vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (slot.unlocked) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.evidence_pin_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = stringResource(slot.lineRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 7.5.sp,
                lineHeight = 9.sp,
                letterSpacing = 0.2.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = stringResource(R.string.evidence_locked_mark),
                style = MaterialTheme.typography.headlineMedium,
                color = ChromeCyan.copy(alpha = 0.55f)
            )
            Text(
                text = stringResource(slot.hintRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 7.5.sp,
                lineHeight = 9.sp,
                letterSpacing = 0.2.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
