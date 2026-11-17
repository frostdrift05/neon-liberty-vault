package com.Meztido0.Dewgov.core.di

import android.content.Context
import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.data.repository.CityRepositoryImpl
import com.Meztido0.Dewgov.data.repository.EvidenceRepositoryImpl
import com.Meztido0.Dewgov.data.repository.ProgressRepositoryImpl
import com.Meztido0.Dewgov.data.repository.UpgradeRepositoryImpl
import com.Meztido0.Dewgov.domain.repository.CityRepository
import com.Meztido0.Dewgov.domain.repository.EvidenceRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import com.Meztido0.Dewgov.domain.repository.UpgradeRepository
import com.Meztido0.Dewgov.domain.usecase.ApplyMissionChoiceUseCase
import com.Meztido0.Dewgov.domain.usecase.GenerateNightUseCase
import com.Meztido0.Dewgov.domain.usecase.PatrolSweepUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveEndingUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveJobUseCase

object ServiceLocator {

    private var appContext: Context? = null

    fun init(context: Context) {
        if (appContext == null) appContext = context.applicationContext
    }

    private val store: PreferencesStore by lazy {
        PreferencesStore(requireNotNull(appContext))
    }

    val cityRepository: CityRepository by lazy { CityRepositoryImpl() }
    val progressRepository: ProgressRepository by lazy { ProgressRepositoryImpl(store) }
    val upgradeRepository: UpgradeRepository by lazy { UpgradeRepositoryImpl(store) }
    val evidenceRepository: EvidenceRepository by lazy { EvidenceRepositoryImpl(store) }

    val generateNight: GenerateNightUseCase by lazy { GenerateNightUseCase() }
    val resolveJob: ResolveJobUseCase by lazy { ResolveJobUseCase() }
    val patrolSweep: PatrolSweepUseCase by lazy { PatrolSweepUseCase() }
    val applyMissionChoice: ApplyMissionChoiceUseCase by lazy { ApplyMissionChoiceUseCase() }
    val resolveEnding: ResolveEndingUseCase by lazy { ResolveEndingUseCase() }
}
