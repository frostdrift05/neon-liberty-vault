package com.Meztido0.Dewgov.presentation.garage

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.domain.repository.CityRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import com.Meztido0.Dewgov.domain.repository.UpgradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GarageViewModel(
    private val cityRepository: CityRepository,
    private val progressRepository: ProgressRepository,
    private val upgradeRepository: UpgradeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GarageUiState())
    val state: StateFlow<GarageUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        val owned = upgradeRepository.owned()
        val slots = cityRepository.upgrades().map { upgrade ->
            UpgradeSlot(
                id = upgrade.id,
                nameRes = upgrade.nameRes,
                effectRes = upgrade.effectRes,
                cost = upgrade.cost,
                owned = owned.contains(upgrade.id),
                affordable = progress.bankedChips >= upgrade.cost
            )
        }
        _state.update {
            it.copy(
                bankedText = formatChips(progress.bankedChips),
                bankedValue = progress.bankedChips,
                slots = slots
            )
        }
    }

    fun buy(upgradeId: Int) {
        val progress = progressRepository.load()
        val upgrade = cityRepository.upgrades().firstOrNull { it.id == upgradeId } ?: return
        if (upgradeRepository.owned().contains(upgradeId)) return
        if (progress.bankedChips < upgrade.cost) return
        upgradeRepository.install(upgradeId)
        progressRepository.save(progress.copy(bankedChips = progress.bankedChips - upgrade.cost))
        refresh()
    }
}
