package com.Meztido0.Dewgov.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.core.format.formatStreak
import com.Meztido0.Dewgov.domain.model.NightResult
import com.Meztido0.Dewgov.domain.repository.CityRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameOverViewModel(
    private val cityRepository: CityRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    fun present(result: NightResult) {
        val ending = cityRepository.endings().firstOrNull { it.id == result.endingId }
        val progress = progressRepository.load()
        val stats = buildList {
            if (result.banked > 0) {
                add(
                    ResultStat(
                        R.string.result_take_banked,
                        formatChips(result.banked),
                        StatAccent.AMBER
                    )
                )
            }
            if (result.heatAtExit > 0) {
                add(
                    ResultStat(
                        R.string.result_heat_exit,
                        result.heatAtExit.toString(),
                        StatAccent.MAGENTA
                    )
                )
            }
            if (result.districtsHit > 0) {
                add(
                    ResultStat(
                        R.string.result_districts,
                        result.districtsHit.toString(),
                        StatAccent.CYAN
                    )
                )
            }
            if (result.bestStreakX10 > 10) {
                add(
                    ResultStat(
                        R.string.result_best_streak,
                        formatStreak(result.bestStreakX10),
                        StatAccent.CYAN
                    )
                )
            }
        }
        _state.update {
            it.copy(
                ready = true,
                endingId = result.endingId,
                titleRes = ending?.titleRes ?: R.string.ending_4_title,
                epilogueRes = ending?.textRes ?: R.string.ending_4_text,
                bankedValue = result.banked,
                stats = if (stats.size >= 2) stats else emptyList(),
                newPins = result.newPins.size,
                lostText = formatChips(result.lostTake),
                lostValue = result.lostTake,
                endingsSeen = progress.endingsSeen
            )
        }
    }
}
