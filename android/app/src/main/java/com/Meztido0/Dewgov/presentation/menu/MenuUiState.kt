package com.Meztido0.Dewgov.presentation.menu

import com.Meztido0.Dewgov.R

data class MenuUiState(
    val bankedChips: String = "0",
    val bestNight: String = "0",
    val bestNightValue: Int = 0,
    val nightsRun: Int = 0,
    val reputationValue: Int = 0,
    val reputationSigned: String = "0",
    val reputationBandRes: Int = R.string.rep_unaligned,
    val upgradesOwned: Int = 0,
    val pinsFound: Int = 0,
    val endingsSeen: Set<Int> = emptySet()
) {
    val hasHistory: Boolean
        get() = nightsRun > 0 && bestNightValue > 0
}
