package com.Meztido0.Dewgov.domain.model

object EndingIds {
    const val BUSTED = 0
    const val GHOST_EXIT = 1
    const val CROWN_TAKE = 2
    const val BURNED_DEAL = 3
    const val EMPTY_VAULT = 4
}

object UpgradeIds {
    const val CHROME_PLATING = 0
    const val SIGNAL_SCRAMBLER = 1
    const val NITROUS_INJECTOR = 2
    const val ARMORED_TRUNK = 3
}

enum class EndingCause { BUST, BREACH, OUT_OF_MOVES, WALK_AWAY }
