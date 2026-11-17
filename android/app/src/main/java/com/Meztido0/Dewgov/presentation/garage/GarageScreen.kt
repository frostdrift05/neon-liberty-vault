package com.Meztido0.Dewgov.presentation.garage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.MagentaHaze
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceHud
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.ui.Glow
import com.Meztido0.Dewgov.core.ui.ScreenBackground
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionTitle
import com.Meztido0.Dewgov.core.ui.hudSurface
import com.Meztido0.Dewgov.presentation.garage.components.UpgradeRow

@Composable
fun GarageRoute(onMenu: () -> Unit) {
    val garageViewModel: GarageViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by garageViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(garageViewModel) {
        garageViewModel.refresh()
    }
    GarageScreen(state = state, onBuy = garageViewModel::buy, onMenu = onMenu)
}

@Composable
fun GarageScreen(
    state: GarageUiState,
    onBuy: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val loop = rememberInfiniteTransition(label = "garageLoop")
    val underGlow by loop.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "underGlow"
    )

    ScreenBackground(
        photoAlpha = 0.46f,
        overlay = listOf(
            AsphaltBlack.copy(alpha = 0.93f),
            SurfaceHud.copy(alpha = 0.82f),
            MagentaHaze.copy(alpha = 0.92f)
        ),
        glows = listOf(Glow(ChromeCyan, 0.16f, 0.5f, 0.3f, 0.7f))
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
                    text = stringResource(R.string.garage_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .height(34.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = state.bankedText,
                        style = MaterialTheme.typography.labelLarge,
                        color = StreetAmber
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .drawBehind {
                        drawOval(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ChromeCyan.copy(alpha = underGlow * 0.35f),
                                    ChromeCyan.copy(alpha = 0f)
                                ),
                                center = Offset(size.width / 2f, size.height * 0.86f),
                                radius = size.width * 0.42f
                            ),
                            topLeft = Offset(size.width * 0.16f, size.height * 0.68f),
                            size = Size(size.width * 0.68f, size.height * 0.3f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.garage_car_cd),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(140.dp)
                )
            }

            SectionTitle(text = stringResource(R.string.garage_title), accent = StreetAmber)

            state.slots.forEach { slot ->
                UpgradeRow(
                    slot = slot,
                    onBuy = onBuy,
                    modifier = Modifier.height(84.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
