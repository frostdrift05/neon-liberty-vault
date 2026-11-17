package com.Meztido0.Dewgov.presentation.menu

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.core.format.formatSigned
import com.Meztido0.Dewgov.domain.model.Reputation
import com.Meztido0.Dewgov.domain.repository.EvidenceRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import com.Meztido0.Dewgov.domain.repository.UpgradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val upgradeRepository: UpgradeRepository,
    private val evidenceRepository: EvidenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        val bandRes = when (progress.band) {
            Reputation.STREET -> R.string.rep_street
            Reputation.UNALIGNED -> R.string.rep_unaligned
            Reputation.SYNDICATE -> R.string.rep_syndicate
        }
        _state.update {
            it.copy(
                bankedChips = formatChips(progress.bankedChips),
                bestNight = formatChips(progress.bestNight),
                bestNightValue = progress.bestNight,
                nightsRun = progress.nightsRun,
                reputationValue = progress.reputation,
                reputationSigned = formatSigned(progress.reputation),
                reputationBandRes = bandRes,
                upgradesOwned = upgradeRepository.owned().size,
                pinsFound = evidenceRepository.pins().size,
                endingsSeen = progress.endingsSeen
            )
        }
    }
}
