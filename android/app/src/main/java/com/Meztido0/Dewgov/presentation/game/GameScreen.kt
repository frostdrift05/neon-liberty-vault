package com.Meztido0.Dewgov.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.PatrolRed
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextFaint
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.Glow
import com.Meztido0.Dewgov.core.ui.MeterBar
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.domain.model.NightResult
import com.Meztido0.Dewgov.presentation.dialog.AbortDialog
import com.Meztido0.Dewgov.presentation.dialog.MissionDialog
import com.Meztido0.Dewgov.presentation.game.components.CrewChip
import com.Meztido0.Dewgov.presentation.game.components.FloatingControlBar
import com.Meztido0.Dewgov.presentation.game.components.ResolveCard
import com.Meztido0.Dewgov.presentation.game.components.TacticalMap

@Composable
fun GameRoute(
    token: Int,
    onGameOver: (NightResult) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(token) {
        gameViewModel.beginNight(token)
    }
    LaunchedEffect(gameViewModel) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.NightOver -> onGameOver(event.result)
            }
        }
    }
    DisposableEffect(gameViewModel) {
        onDispose { gameViewModel.pause() }
    }
    GameScreen(
        state = state,
        onSelectDistrict = gameViewModel::selectDistrict,
        onSelectCrew = gameViewModel::selectCrew,
        onDecode = gameViewModel::decode,
        onGo = gameViewModel::go,
        onBank = gameViewModel::bank,
        onPause = gameViewModel::openAbort,
        onResume = gameViewModel::dismissAbort,
        onWalkAway = gameViewModel::walkAway,
        onMissionChoice = gameViewModel::chooseMission
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onSelectDistrict: (Int) -> Unit,
    onSelectCrew: (Int) -> Unit,
    onDecode: () -> Unit,
    onGo: () -> Unit,
    onBank: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onWalkAway: () -> Unit,
    onMissionChoice: (Boolean) -> Unit
) {
    val loop = rememberInfiniteTransition(label = "gameLoop")
    val nodeGlow by loop.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nodeGlow"
    )
    val travel by loop.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "travel"
    )
    val takeShown by animateIntAsState(
        targetValue = state.takeValue,
        animationSpec = tween(durationMillis = 600),
        label = "takeRoll"
    )

    val endingTint = if (state.endingTintBusted) PatrolRed else NeonMagenta

    ScreenBackground(
        photoAlpha = 0.52f,
        overlay = listOf(
            AsphaltBlack.copy(alpha = 0.93f),
            SurfaceHud.copy(alpha = 0.8f),
            MagentaHaze.copy(alpha = 0.93f)
        ),
        glows = listOf(
            Glow(endingTint, if (state.phase == GamePhase.ENDING) 0.4f else 0.14f, 0.5f, 0.42f, 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                TacticalMap(
                    nodes = state.nodes,
                    currentIndex = state.currentIndex,
                    glow = nodeGlow,
                    travel = travel,
                    onSelect = onSelectDistrict,
                    modifier = Modifier.fillMaxSize()
                )
                if (state.resolve != null) {
                    ResolveCard(
                        resolve = state.resolve,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            TakeStrip(takeShown = takeShown, state = state)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.crew.forEach { slot ->
                    CrewChip(
                        slot = slot,
                        onSelect = onSelectCrew,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                }
            }

            Text(
                text = stringResource(state.hintRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            )

            FloatingControlBar(
                decodeCharges = state.decodeCharges,
                canDecode = state.canDecode,
                canGo = state.canGo,
                canBank = state.canBank,
                onDecode = onDecode,
                onGo = onGo,
                onBank = onBank,
                modifier = Modifier.padding(start = 18.dp, end = 18.dp, bottom = 16.dp)
            )
        }
    }

    if (state.mission != null) {
        MissionDialog(mission = state.mission, onChoose = onMissionChoice)
    }
    if (state.abortVisible) {
        AbortDialog(onResume = onResume, onWalkAway = onWalkAway)
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .background(SurfaceHud.copy(alpha = 0.88f))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onPause),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.game_pause_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_clock_fmt, state.clockHour),
                style = MaterialTheme.typography.titleMedium,
                color = ChromeCyan
            )
            Text(
                text = stringResource(R.string.game_move_fmt, state.moveNumber, state.movesTotal),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = stringResource(R.string.game_seed_fmt, state.seed),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = TextFaint
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(104.dp)
        ) {
            Text(
                text = stringResource(R.string.game_heat_fmt, state.heat, state.heatCap),
                style = MaterialTheme.typography.labelSmall,
                color = if (state.heat >= GameConfig.PATROL_HEAT_ONE) PatrolRed else TextSecondary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeterBar(
                value = state.heat,
                max = state.heatCap,
                fill = Brush.horizontalGradient(listOf(NeonMagenta, PatrolRed)),
                barHeight = 8.dp,
                markerAt = GameConfig.PATROL_HEAT_ONE.toFloat() / state.heatCap.toFloat(),
                markerColor = StreetAmber,
                polite = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TakeStrip(takeShown: Int, state: GameUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .hudSurface(
                    borderColor = StreetAmber,
                    borderAlpha = 0.45f,
                    fillAlpha = 0.85f,
                    corner = 6.dp,
                    bracketArm = 9.dp,
                    bracketAlpha = 0.75f
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.game_chips_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.game_take_fmt, formatChips(takeShown)),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 13.sp,
                color = StreetAmber,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(
                    if (state.streakX10 > GameConfig.STREAK_MIN_X10) {
                        ChromeCyan.copy(alpha = 0.16f)
                    } else {
                        SurfaceHud.copy(alpha = 0.7f)
                    },
                    RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.game_streak_fmt, state.streakText),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 13.sp,
                color = if (state.streakX10 > GameConfig.STREAK_MIN_X10) ChromeCyan else TextMuted
            )
        }
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(SurfaceHud.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.bankedText,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 13.sp,
                color = TextPrimary
            )
        }
    }
}
