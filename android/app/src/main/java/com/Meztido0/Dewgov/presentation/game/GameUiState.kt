package com.Meztido0.Dewgov.presentation.game

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.JobOutcome
import com.Meztido0.Dewgov.domain.model.JobType
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.NightResult

enum class GamePhase { PLANNING, RESOLVING, MISSION, ENDING }

enum class NodeState { CURRENT, SELECTED, REACHABLE, VISITED, LOCKED, IDLE }

data class NodeUi(
    val index: Int,
    val nameRes: Int,
    val row: Int,
    val col: Int,
    val jobType: JobType,
    val isVault: Boolean,
    val isFence: Boolean,
    val risk: Int,
    val riskLow: Int,
    val riskHigh: Int,
    val payout: Int,
    val decoded: Boolean,
    val hasMission: Boolean,
    val patrolTarget: Boolean,
    val state: NodeState
)

data class CrewSlot(
    val id: Int,
    val nameRes: Int,
    val jobType: JobType,
    val nerve: Int,
    val enabled: Boolean,
    val selected: Boolean,
    val matchesTarget: Boolean
)

data class ResolveUi(
    val outcome: JobOutcome,
    val amount: String,
    val banking: Boolean = false
)

data class GameUiState(
    val ready: Boolean = false,
    val phase: GamePhase = GamePhase.PLANNING,
    val seed: Long = 0L,
    val nodes: List<NodeUi> = emptyList(),
    val crew: List<CrewSlot> = emptyList(),
    val currentIndex: Int = GameConfig.START_INDEX,
    val selectedIndex: Int = -1,
    val movesLeft: Int = GameConfig.NIGHT_MOVES,
    val movesTotal: Int = GameConfig.NIGHT_MOVES,
    val moveNumber: Int = 1,
    val clockHour: Int = GameConfig.NIGHT_START_HOUR,
    val heat: Int = 0,
    val heatCap: Int = GameConfig.HEAT_CAP_BASE,
    val takeText: String = "0",
    val takeValue: Int = 0,
    val bankedText: String = "0",
    val streakText: String = "1.0",
    val streakX10: Int = GameConfig.STREAK_MIN_X10,
    val decodeCharges: Int = GameConfig.DECODE_CHARGES,
    val canDecode: Boolean = false,
    val canGo: Boolean = false,
    val canBank: Boolean = false,
    val hintRes: Int = R.string.hint_plan,
    val resolve: ResolveUi? = null,
    val mission: Mission? = null,
    val abortVisible: Boolean = false,
    val endingTintBusted: Boolean = false
)

sealed interface GameEvent {
    data class NightOver(val result: NightResult) : GameEvent
}
