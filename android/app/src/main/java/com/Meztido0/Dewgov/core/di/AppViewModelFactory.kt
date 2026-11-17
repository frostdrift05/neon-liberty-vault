package com.Meztido0.Dewgov.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Meztido0.Dewgov.presentation.app.AppViewModel
import com.Meztido0.Dewgov.presentation.evidence.EvidenceViewModel
import com.Meztido0.Dewgov.presentation.game.GameViewModel
import com.Meztido0.Dewgov.presentation.gameover.GameOverViewModel
import com.Meztido0.Dewgov.presentation.garage.GarageViewModel
import com.Meztido0.Dewgov.presentation.menu.MenuViewModel
import com.Meztido0.Dewgov.presentation.splash.SplashViewModel

object AppViewModelFactory {

    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { AppViewModel() }
        initializer { SplashViewModel() }
        initializer {
            MenuViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.upgradeRepository,
                ServiceLocator.evidenceRepository
            )
        }
        initializer {
            GameViewModel(
                ServiceLocator.cityRepository,
                ServiceLocator.progressRepository,
                ServiceLocator.upgradeRepository,
                ServiceLocator.evidenceRepository,
                ServiceLocator.generateNight,
                ServiceLocator.resolveJob,
                ServiceLocator.patrolSweep,
                ServiceLocator.applyMissionChoice,
                ServiceLocator.resolveEnding
            )
        }
        initializer {
            GameOverViewModel(
                ServiceLocator.cityRepository,
                ServiceLocator.progressRepository
            )
        }
        initializer {
            GarageViewModel(
                ServiceLocator.cityRepository,
                ServiceLocator.progressRepository,
                ServiceLocator.upgradeRepository
            )
        }
        initializer {
            EvidenceViewModel(
                ServiceLocator.cityRepository,
                ServiceLocator.evidenceRepository,
                ServiceLocator.progressRepository
            )
        }
    }
}
