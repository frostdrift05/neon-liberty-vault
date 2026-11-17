package com.Meztido0.Dewgov.domain.model

data class Progress(
    val bankedChips: Int = 0,
    val bestNight: Int = 0,
    val nightsRun: Int = 0,
    val reputation: Int = 0,
    val endingsSeen: Set<Int> = emptySet(),
    val vaultCodeKnown: Boolean = false
) {
    val band: Reputation
        get() = when {
            reputation <= -3 -> Reputation.STREET
            reputation >= 3 -> Reputation.SYNDICATE
            else -> Reputation.UNALIGNED
        }
}
