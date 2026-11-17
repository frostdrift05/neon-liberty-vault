package com.Meztido0.Dewgov.presentation.garage

data class UpgradeSlot(
    val id: Int,
    val nameRes: Int,
    val effectRes: Int,
    val cost: Int,
    val owned: Boolean,
    val affordable: Boolean
)

data class GarageUiState(
    val bankedText: String = "0",
    val bankedValue: Int = 0,
    val slots: List<UpgradeSlot> = emptyList()
)
