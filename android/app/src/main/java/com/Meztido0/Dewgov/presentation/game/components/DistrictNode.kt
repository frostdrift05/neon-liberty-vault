package com.Meztido0.Dewgov.presentation.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import com.Meztido0.Dewgov.core.theme.PatrolRed
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceAlt
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.core.ui.jobIconRes
import com.Meztido0.Dewgov.presentation.game.NodeState
import com.Meztido0.Dewgov.presentation.game.NodeUi

@Composable
fun DistrictNode(
    node: NodeUi,
    glow: Float,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = when (node.state) {
        NodeState.CURRENT -> NeonMagenta
        NodeState.SELECTED -> ChromeCyan
        NodeState.REACHABLE -> ChromeCyan
        NodeState.LOCKED -> PatrolRed
        NodeState.VISITED -> BorderDim
        NodeState.IDLE -> BorderDim
    }
    val selectable = node.state == NodeState.REACHABLE || node.state == NodeState.SELECTED
    val borderAlpha = when (node.state) {
        NodeState.CURRENT -> glow
        NodeState.SELECTED -> 1f
        NodeState.REACHABLE -> 0.6f
        NodeState.LOCKED -> 0.85f
        else -> 0.35f
    }
    val fillColor = if (node.state == NodeState.SELECTED) SurfaceAlt else SurfaceHud
    val dim = when (node.state) {
        NodeState.LOCKED -> 0.45f
        NodeState.VISITED -> 0.6f
        NodeState.IDLE -> 0.8f
        else -> 1f
    }
    val stateLabel = when (node.state) {
        NodeState.CURRENT -> stringResource(R.string.state_current)
        NodeState.SELECTED -> stringResource(R.string.state_selected)
        NodeState.LOCKED -> stringResource(R.string.state_locked)
        NodeState.VISITED -> stringResource(R.string.state_visited)
        else -> stringResource(R.string.job_vault)
    }
    Box(
        modifier = modifier
            .hudSurface(
                borderColor = accent,
                borderAlpha = borderAlpha,
                fillColor = fillColor,
                fillAlpha = 0.9f,
                corner = 6.dp,
                bracketArm = 11.dp,
                bracketAlpha = borderAlpha,
                bracketStroke = if (node.state == NodeState.SELECTED) 2.dp else 1.5.dp
            )
            .clickable(enabled = selectable) { onSelect(node.index) }
            .semantics { stateDescription = stateLabel }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 6.dp)
                .alpha(dim),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(node.nameRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.5.sp,
                lineHeight = 10.sp,
                letterSpacing = 0.6.sp,
                color = if (node.state == NodeState.IDLE) TextMuted else TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            if (node.isVault) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.game_vault_cd),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
            } else {
                Icon(
                    painter = painterResource(jobIconRes(node.jobType)),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(18.dp)
                )
            }
            RiskPips(risk = node.risk, decoded = node.decoded, low = node.riskLow, high = node.riskHigh)
            Text(
                text = if (node.isVault) {
                    stringResource(R.string.node_breach)
                } else if (node.decoded) {
                    stringResource(R.string.node_payout_exact_fmt, node.payout)
                } else {
                    stringResource(R.string.node_payout_guess_fmt, node.payout)
                },
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = StreetAmber,
                maxLines = 1
            )
        }
        if (node.state == NodeState.CURRENT) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.game_car_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 4.dp)
                    .size(30.dp)
            )
        }
        if (node.hasMission) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = stringResource(R.string.game_antenna_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 3.dp, top = 3.dp)
                    .size(14.dp)
            )
        }
        if (node.state == NodeState.LOCKED) {
            Icon(
                painter = painterResource(R.drawable.ic_cross),
                contentDescription = null,
                tint = PatrolRed,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(26.dp)
            )
        }
        if (node.state == NodeState.VISITED) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = ChromeCyan.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 3.dp, top = 3.dp)
                    .size(12.dp)
            )
        }
    }
}

@Composable
private fun RiskPips(risk: Int, decoded: Boolean, low: Int, high: Int) {
    val filled = if (decoded) risk else low
    val ghost = if (decoded) risk else high
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(8.dp)
    ) {
        for (i in 1..5) {
            val color: Color = when {
                i <= filled -> NeonMagenta
                i <= ghost -> NeonMagenta.copy(alpha = 0.35f)
                else -> BorderDim
            }
            Spacer(
                modifier = Modifier
                    .size(5.dp)
                    .background(color, CircleShape)
            )
        }
    }
}
