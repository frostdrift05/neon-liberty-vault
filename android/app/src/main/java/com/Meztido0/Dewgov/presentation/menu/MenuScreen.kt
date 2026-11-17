package com.Meztido0.Dewgov.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.EndingDiamondRow
import com.Meztido0.Dewgov.core.ui.Glow
import com.Meztido0.Dewgov.core.ui.HudPanel
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.StatCard
import com.Meztido0.Dewgov.core.ui.hudSurface

@Composable
fun MenuRoute(
    onPlay: () -> Unit,
    onGarage: () -> Unit,
    onEvidence: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(menuViewModel) {
        menuViewModel.refresh()
    }
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onGarage = onGarage,
        onEvidence = onEvidence
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onGarage: () -> Unit,
    onEvidence: () -> Unit
) {
    val loop = rememberInfiniteTransition(label = "menuLoop")
    val borderPulse by loop.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menuBorder"
    )

    ScreenBackground(
        photoAlpha = 0.75f,
        overlay = listOf(
            AsphaltBlack.copy(alpha = 0.9f),
            SurfaceHud.copy(alpha = 0.66f),
            MagentaHaze.copy(alpha = 0.9f)
        ),
        glows = listOf(
            Glow(NeonMagenta, 0.2f, 0.85f, 0.12f, 0.75f),
            Glow(ChromeCyan, 0.14f, 0.1f, 0.88f, 0.62f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuHeader(banked = state.bankedChips)

            PlayTile(
                borderPulse = borderPulse,
                onPlay = onPlay
            )

            if (state.hasHistory) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.menu_best_night),
                        value = state.bestNight,
                        accent = StreetAmber,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                    StatCard(
                        label = stringResource(R.string.menu_reputation),
                        value = stringResource(
                            R.string.rep_value_fmt,
                            stringResource(state.reputationBandRes),
                            state.reputationSigned
                        ),
                        accent = if (state.reputationValue < 0) NeonMagenta else ChromeCyan,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                }
            } else {
                HudPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp),
                    borderColor = ChromeCyan,
                    borderAlpha = 0.3f,
                    bracketAlpha = 0.6f
                ) {
                    Text(
                        text = stringResource(R.string.menu_empty_runs),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 18.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RouteTile(
                    title = stringResource(R.string.garage),
                    caption = stringResource(
                        R.string.menu_upgrades_fmt,
                        state.upgradesOwned,
                        GameConfig.TOTAL_UPGRADES
                    ),
                    iconRes = R.drawable.icon_home,
                    accent = ChromeCyan,
                    onClick = onGarage,
                    modifier = Modifier.weight(1f)
                )
                RouteTile(
                    title = stringResource(R.string.evidence),
                    caption = stringResource(
                        R.string.menu_pins_fmt,
                        state.pinsFound,
                        GameConfig.TOTAL_PINS
                    ),
                    iconRes = R.drawable.sprite_5,
                    accent = StreetAmber,
                    onClick = onEvidence,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomStrip(endingsSeen = state.endingsSeen)
        }
    }
}

@Composable
private fun MenuHeader(banked: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                maxLines = 2
            )
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = ChromeCyan,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Row(
            modifier = Modifier
                .height(36.dp)
                .hudSurface(
                    borderColor = StreetAmber,
                    borderAlpha = 0.45f,
                    fillAlpha = 0.85f,
                    corner = 6.dp,
                    bracketArm = 8.dp,
                    bracketAlpha = 0.8f
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.menu_chips_cd),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = banked,
                style = MaterialTheme.typography.labelLarge,
                color = StreetAmber
            )
        }
    }
}

@Composable
private fun PlayTile(borderPulse: Float, onPlay: () -> Unit) {
    Button(
        onClick = onPlay,
        shape = RoundedCornerShape(10.dp),
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
            .hudSurface(
                borderColor = NeonMagenta,
                borderAlpha = borderPulse,
                fillAlpha = 0.9f,
                corner = 10.dp,
                bracketArm = 18.dp,
                bracketAlpha = borderPulse
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(96.dp)
                    .graphicsLayer { alpha = 0.96f }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.play),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 30.sp,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.menu_play_sub),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun RouteTile(
    title: String,
    caption: String,
    iconRes: Int,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        SecondaryButton(
            text = title,
            onClick = onClick,
            leadingIcon = iconRes,
            accent = accent,
            iconSize = 22.dp,
            minHeight = 50.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BottomStrip(endingsSeen: Set<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceHud.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.menu_endings_fmt,
                    endingsSeen.size,
                    GameConfig.TOTAL_ENDINGS
                ),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            EndingDiamondRow(endingsSeen = endingsSeen)
        }
        Text(
            text = stringResource(R.string.menu_disclaimer),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

