package com.Meztido0.Dewgov.presentation.evidence

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.repository.CityRepository
import com.Meztido0.Dewgov.domain.repository.EvidenceRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EvidenceViewModel(
    private val cityRepository: CityRepository,
    private val evidenceRepository: EvidenceRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EvidenceUiState())
    val state: StateFlow<EvidenceUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val unlocked = evidenceRepository.pins()
        val progress = progressRepository.load()
        val slots = cityRepository.dossiers().map { dossier ->
            PinSlot(
                id = dossier.id,
                lineRes = dossier.lineRes,
                hintRes = dossier.hintRes,
                linkedId = dossier.linkedId,
                unlocked = unlocked.contains(dossier.id)
            )
        }
        _state.update {
            it.copy(
                slots = slots,
                found = unlocked.size,
                total = GameConfig.TOTAL_PINS,
                vaultCodeKnown = unlocked.size >= GameConfig.VAULT_CODE_PINS,
                endingsSeen = progress.endingsSeen
            )
        }
    }
}
