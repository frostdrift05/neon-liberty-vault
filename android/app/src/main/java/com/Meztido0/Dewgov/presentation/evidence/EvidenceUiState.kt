package com.Meztido0.Dewgov.presentation.evidence

data class PinSlot(
    val id: Int,
    val lineRes: Int,
    val hintRes: Int,
    val linkedId: Int,
    val unlocked: Boolean
)

data class EvidenceUiState(
    val slots: List<PinSlot> = emptyList(),
    val found: Int = 0,
    val total: Int = 0,
    val vaultCodeKnown: Boolean = false,
    val endingsSeen: Set<Int> = emptySet()
)
