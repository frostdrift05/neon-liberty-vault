package com.Meztido0.Dewgov.presentation.gameover

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.domain.model.EndingIds

enum class StatAccent { AMBER, CYAN, MAGENTA }

data class ResultStat(
    val labelRes: Int,
    val value: String,
    val accent: StatAccent
)

data class GameOverUiState(
    val ready: Boolean = false,
    val endingId: Int = EndingIds.EMPTY_VAULT,
    val titleRes: Int = R.string.ending_4_title,
    val epilogueRes: Int = R.string.ending_4_text,
    val bankedValue: Int = 0,
    val stats: List<ResultStat> = emptyList(),
    val newPins: Int = 0,
    val lostText: String = "0",
    val lostValue: Int = 0,
    val endingsSeen: Set<Int> = emptySet()
)
