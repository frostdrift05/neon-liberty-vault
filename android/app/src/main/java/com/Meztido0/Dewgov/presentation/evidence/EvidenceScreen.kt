package com.Meztido0.Dewgov.presentation.evidence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceAlt
import com.Meztido0.Dewgov.core.theme.SurfaceRaised
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.EndingDiamondRow
import com.Meztido0.Dewgov.core.ui.Glow
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.presentation.evidence.components.EvidencePinCard

@Composable
fun EvidenceRoute(onMenu: () -> Unit) {
    val evidenceViewModel: EvidenceViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by evidenceViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(evidenceViewModel) {
        evidenceViewModel.refresh()
    }
    EvidenceScreen(state = state, onMenu = onMenu)
}

@Composable
fun EvidenceScreen(
    state: EvidenceUiState,
    onMenu: () -> Unit
) {
    ScreenBackground(
        photoAlpha = 0.44f,
        overlay = listOf(
            AsphaltBlack.copy(alpha = 0.94f),
            SurfaceAlt.copy(alpha = 0.72f),
            MagentaHaze.copy(alpha = 0.93f)
        ),
        glows = listOf(Glow(NeonMagenta, 0.16f, 0.2f, 0.25f, 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    leadingIcon = R.drawable.icon_home,
                    accent = ChromeCyan,
                    minHeight = 48.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.evidence_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.evidence_pins_fmt, state.found, state.total),
                    style = MaterialTheme.typography.labelSmall,
                    color = StreetAmber
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .hudSurface(
                        borderColor = ChromeCyan,
                        borderAlpha = 0.35f,
                        fillColor = SurfaceAlt,
                        fillAlpha = 0.72f,
                        corner = 10.dp,
                        bracketArm = 16.dp,
                        bracketAlpha = 0.7f
                    )
                    .padding(10.dp)
            ) {
                if (state.found == 0) {
                    Text(
                        text = stringResource(R.string.evidence_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp)
                    )
                } else {
                    PinBoard(slots = state.slots)
                }
            }

            LinearProgressIndicator(
                progress = { if (state.total == 0) 0f else state.found.toFloat() / state.total.toFloat() },
                color = NeonMagenta,
                trackColor = SurfaceRaised,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )

            Text(
                text = stringResource(R.string.evidence_note),
                style = MaterialTheme.typography.labelSmall,
                color = if (state.vaultCodeKnown) StreetAmber else TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            EndingDiamondRow(
                endingsSeen = state.endingsSeen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun PinBoard(slots: List<PinSlot>) {
    val rows = slots.chunked(3)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { slot ->
                    EvidencePinCard(
                        slot = slot,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}
