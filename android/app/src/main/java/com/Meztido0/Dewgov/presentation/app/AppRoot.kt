package com.Meztido0.Dewgov.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.core.di.AppViewModelFactory
import com.Meztido0.Dewgov.core.navigation.Screen
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.presentation.evidence.EvidenceRoute
import com.Meztido0.Dewgov.presentation.game.GameRoute
import com.Meztido0.Dewgov.presentation.gameover.GameOverRoute
import com.Meztido0.Dewgov.presentation.garage.GarageRoute
import com.Meztido0.Dewgov.presentation.menu.MenuRoute
import com.Meztido0.Dewgov.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()

    BackHandler(enabled = backStack.isNotEmpty()) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AsphaltBlack)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 220)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 220))
            },
            label = "screenTransition"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashRoute(onFinished = appViewModel::onSplashFinished)
                is Screen.Menu -> MenuRoute(
                    onPlay = appViewModel::openGame,
                    onGarage = appViewModel::openGarage,
                    onEvidence = appViewModel::openEvidence
                )
                is Screen.Game -> GameRoute(
                    token = current.token,
                    onGameOver = appViewModel::onNightOver
                )
                is Screen.GameOver -> GameOverRoute(
                    result = current.result,
                    onPlayAgain = appViewModel::playAgain,
                    onMenu = appViewModel::toMenu
                )
                is Screen.Garage -> GarageRoute(onMenu = appViewModel::toMenu)
                is Screen.Evidence -> EvidenceRoute(onMenu = appViewModel::toMenu)
            }
        }
    }
}
