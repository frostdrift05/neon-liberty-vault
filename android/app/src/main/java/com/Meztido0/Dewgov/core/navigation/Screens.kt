package com.Meztido0.Dewgov.core.navigation

import com.Meztido0.Dewgov.domain.model.NightResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val token: Int) : Screen
    data class GameOver(val result: NightResult) : Screen
    data object Garage : Screen
    data object Evidence : Screen
}
