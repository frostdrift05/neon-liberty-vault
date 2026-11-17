package com.Meztido0.Dewgov.presentation.garage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.presentation.garage.UpgradeSlot

@Composable
fun UpgradeRow(
    slot: UpgradeSlot,
    onBuy: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = if (slot.owned) StreetAmber else ChromeCyan
    val stateLabel = when {
        slot.owned -> stringResource(R.string.state_visited)
        !slot.affordable -> stringResource(R.string.state_locked)
        else -> stringResource(R.string.state_selected)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .hudSurface(
                borderColor = if (slot.owned) StreetAmber else BorderDim,
                borderAlpha = if (slot.owned) 0.6f else 0.5f,
                fillAlpha = 0.86f,
                corner = 8.dp,
                bracketArm = 11.dp,
                bracketAlpha = if (slot.owned) 0.9f else 0.45f
            )
            .semantics { stateDescription = stateLabel }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(
                if (slot.owned) R.drawable.ic_check else R.drawable.ic_lock
            ),
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(slot.nameRes),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(slot.effectRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        if (slot.owned) {
            Text(
                text = stringResource(R.string.garage_installed),
                style = MaterialTheme.typography.labelSmall,
                color = StreetAmber,
                modifier = Modifier.width(88.dp)
            )
        } else {
            PrimaryButton(
                text = if (slot.affordable) {
                    stringResource(R.string.garage_buy_fmt, slot.cost)
                } else {
                    stringResource(R.string.garage_need_fmt, slot.cost)
                },
                onClick = { onBuy(slot.id) },
                enabled = slot.affordable,
                accentStart = StreetAmber,
                accentEnd = ChromeCyan,
                modifier = Modifier.width(104.dp)
            )
        }
    }
}
