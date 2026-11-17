package com.Meztido0.Dewgov.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val GRID_SIZE = 3
    const val DISTRICT_COUNT = 9
    const val START_INDEX = 7
    const val VAULT_INDEX = 4

    const val NIGHT_MOVES = 8
    const val NIGHT_START_HOUR = 22

    const val HEAT_CAP_BASE = 100
    const val HEAT_CAP_PLATED = 125
    const val PATROL_HEAT_ONE = 60
    const val PATROL_HEAT_TWO = 85
    const val BANK_HEAT_COST = 5

    const val DECODE_CHARGES = 3

    const val NERVE_MAX = 100
    const val NERVE_LEAD_COST = 25
    const val NERVE_BENCH_GAIN = 15
    const val NERVE_BURN_EXTRA = 15
    const val NERVE_MIN_TO_LEAD = 25

    const val STREAK_MIN_X10 = 10
    const val STREAK_STEP_X10 = 5
    const val STREAK_MAX_X10 = 20

    const val CROWN_TAKE_MIN = 6000
    const val CROWN_REP_MIN = 3
    const val GHOST_HEAT_MAX = 40
    const val BURNED_DEAL_REP = -3
    const val BURNED_DEAL_CUT = 30

    const val VAULT_CODE_PINS = 8
    const val TOTAL_PINS = 15
    const val TOTAL_ENDINGS = 5
    const val TOTAL_UPGRADES = 4

    const val SOLVE_ATTEMPTS = 20
    const val MIN_ROUTE_PAYOUT = 4200

    const val RESOLVE_MOVE_MS = 820L
    const val RESOLVE_CARD_MS = 1020L
    const val ENDING_FLASH_MS = 520L

    const val SEED_A = 7919L
    const val SEED_B = 104729L
}
