package com.Meztido0.Dewgov.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.hudSurface

@Composable
fun FloatingControlBar(
    decodeCharges: Int,
    canDecode: Boolean,
    canGo: Boolean,
    canBank: Boolean,
    onDecode: () -> Unit,
    onGo: () -> Unit,
    onBank: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .hudSurface(
                borderColor = ChromeCyan,
                borderAlpha = 0.5f,
                fillAlpha = 0.94f,
                corner = 10.dp,
                bracketArm = 14.dp,
                bracketAlpha = 0.85f
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecondaryButton(
            text = stringResource(R.string.game_decode_fmt, decodeCharges),
            onClick = onDecode,
            enabled = canDecode,
            leadingIcon = R.drawable.sprite_3,
            accent = ChromeCyan,
            iconSize = 20.dp,
            minHeight = 48.dp,
            modifier = Modifier.weight(0.95f)
        )
        PrimaryButton(
            text = stringResource(R.string.go),
            onClick = onGo,
            enabled = canGo,
            leadingIcon = R.drawable.ic_play,
            modifier = Modifier.weight(1.3f)
        )
        if (canBank) {
            SecondaryButton(
                text = stringResource(R.string.bank),
                onClick = onBank,
                enabled = true,
                leadingIcon = R.drawable.sprite_4,
                accent = StreetAmber,
                iconSize = 18.dp,
                minHeight = 48.dp,
                modifier = Modifier.weight(0.8f)
            )
        }
    }
}
