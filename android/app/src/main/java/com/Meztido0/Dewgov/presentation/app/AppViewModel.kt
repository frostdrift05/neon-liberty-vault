package com.Meztido0.Dewgov.presentation.app

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.core.navigation.Screen
import com.Meztido0.Dewgov.domain.model.NightResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    private var nightToken = 0

    fun onSplashFinished() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun openGame() = push(Screen.Game(nextToken()))

    fun openGarage() = push(Screen.Garage)

    fun openEvidence() = push(Screen.Evidence)

    fun onNightOver(result: NightResult) {
        _backStack.value = listOf(Screen.Menu)
        _screen.value = Screen.GameOver(result)
    }

    fun playAgain() {
        _backStack.value = listOf(Screen.Menu)
        _screen.value = Screen.Game(nextToken())
    }

    fun toMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun back() {
        val stack = _backStack.value
        if (stack.isEmpty()) return
        _screen.value = stack[stack.size - 1]
        _backStack.value = stack.dropLast(1)
    }

    private fun nextToken(): Int {
        nightToken += 1
        return nightToken
    }

    private fun push(target: Screen) {
        _backStack.value = _backStack.value + _screen.value
        _screen.value = target
    }
}
