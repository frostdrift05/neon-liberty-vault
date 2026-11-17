package com.Meztido0.Dewgov.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.SectionTitle
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.domain.model.Mission

@Composable
fun MissionDialog(
    mission: Mission,
    onChoose: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = { onChoose(true) },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hudSurface(
                    borderColor = ChromeCyan,
                    borderAlpha = 0.6f,
                    fillAlpha = 0.97f,
                    corner = 10.dp,
                    bracketArm = 16.dp,
                    bracketAlpha = 0.9f
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionTitle(text = stringResource(mission.titleRes), accent = StreetAmber)
            Text(
                text = stringResource(mission.bodyRes),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            PrimaryButton(
                text = stringResource(mission.choiceA.captionRes),
                onClick = { onChoose(true) },
                accentStart = NeonMagenta,
                modifier = Modifier.fillMaxWidth()
            )
            PrimaryButton(
                text = stringResource(mission.choiceB.captionRes),
                onClick = { onChoose(false) },
                accentStart = ChromeCyan,
                accentEnd = StreetAmber,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
