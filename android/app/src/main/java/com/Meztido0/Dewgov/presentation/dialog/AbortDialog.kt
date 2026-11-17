package com.Meztido0.Dewgov.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.PatrolRed
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionTitle
import com.Meztido0.Dewgov.core.ui.hudSurface

@Composable
fun AbortDialog(
    onResume: () -> Unit,
    onWalkAway: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hudSurface(
                    borderColor = PatrolRed,
                    borderAlpha = 0.6f,
                    fillAlpha = 0.97f,
                    corner = 10.dp,
                    bracketArm = 16.dp,
                    bracketAlpha = 0.9f
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionTitle(text = stringResource(R.string.abort_title), accent = PatrolRed)
            Text(
                text = stringResource(R.string.abort_body),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.resume),
                    onClick = onResume,
                    modifier = Modifier.weight(1f)
                )
                PrimaryButton(
                    text = stringResource(R.string.walk_away),
                    onClick = onWalkAway,
                    accentStart = PatrolRed,
                    accentEnd = NeonMagenta,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
