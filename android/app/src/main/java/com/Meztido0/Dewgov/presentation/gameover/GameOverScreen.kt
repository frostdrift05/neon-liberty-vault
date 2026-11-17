package com.Meztido0.Dewgov.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.PatrolRed
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.EndingDiamondRow
import com.Meztido0.Dewgov.core.ui.Glow
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.StatCard
import com.Meztido0.Dewgov.domain.model.EndingIds
import com.Meztido0.Dewgov.domain.model.NightResult

@Composable
fun GameOverRoute(
    result: NightResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(result) {
        gameOverViewModel.present(result)
    }
    GameOverScreen(state = state, onPlayAgain = onPlayAgain, onMenu = onMenu)
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val tint = when (state.endingId) {
        EndingIds.BUSTED -> PatrolRed
        EndingIds.GHOST_EXIT -> ChromeCyan
        EndingIds.CROWN_TAKE -> StreetAmber
        EndingIds.BURNED_DEAL -> NeonMagenta
        else -> SurfaceHud
    }
    val vaultEnding = state.endingId == EndingIds.GHOST_EXIT || state.endingId == EndingIds.CROWN_TAKE
    val loop = rememberInfiniteTransition(label = "resultLoop")
    val halo by loop.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )
    val bankedShown by animateIntAsState(
        targetValue = state.bankedValue,
        animationSpec = tween(durationMillis = 900),
        label = "bankedRoll"
    )

    ScreenBackground(
        photoAlpha = 0.5f,
        overlay = listOf(
            AsphaltBlack.copy(alpha = 0.92f),
            tint.copy(alpha = 0.22f),
            MagentaHaze.copy(alpha = 0.94f)
        ),
        glows = listOf(Glow(tint, 0.26f, 0.5f, 0.3f, 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    if (vaultEnding) R.drawable.sprite_2 else R.drawable.sprite_1
                ),
                contentDescription = stringResource(R.string.result_art_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(104.dp)
                    .graphicsLayer {
                        scaleX = halo
                        scaleY = halo
                    }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(state.titleRes),
                style = MaterialTheme.typography.displaySmall,
                color = tint,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(state.epilogueRes),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = formatChips(bankedShown),
                style = MaterialTheme.typography.displaySmall,
                color = StreetAmber
            )

            if (state.stats.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                StatGrid(stats = state.stats)
            }

            if (state.lostValue > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.result_lost_fmt, state.lostText),
                    style = MaterialTheme.typography.labelSmall,
                    color = PatrolRed
                )
            }

            if (state.newPins > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                NewEvidenceStrip(count = state.newPins)
            }

            Spacer(modifier = Modifier.height(16.dp))

            EndingDiamondRow(endingsSeen = state.endingsSeen, diamondSize = 12.dp)

            Spacer(modifier = Modifier.height(22.dp))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                leadingIcon = R.drawable.ic_play,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingIcon = R.drawable.icon_home,
                accent = ChromeCyan,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatGrid(stats: List<ResultStat>) {
    val rows = stats.chunked(2)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        rows.forEach { row ->
            if (row.size >= 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { stat ->
                        StatCard(
                            label = stringResource(stat.labelRes),
                            value = stat.value,
                            accent = accentColor(stat.accent),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

private fun accentColor(accent: StatAccent): Color = when (accent) {
    StatAccent.AMBER -> StreetAmber
    StatAccent.CYAN -> ChromeCyan
    StatAccent.MAGENTA -> NeonMagenta
}

@Composable
private fun NewEvidenceStrip(count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.result_new_evidence_fmt, count),
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.width(2.dp))
        val shown = if (count > 4) 4 else count
        for (i in 0 until shown) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
