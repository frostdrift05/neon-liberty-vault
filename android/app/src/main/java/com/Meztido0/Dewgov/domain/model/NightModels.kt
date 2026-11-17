package com.Meztido0.Dewgov.domain.model

enum class JobType { WHEELS, WIRES, MUSCLE, WORDS, VAULT }

enum class JobOutcome { CLEAN, CLOSE_CALL, BURNED }

enum class Reputation { STREET, UNALIGNED, SYNDICATE }

data class DistrictBlueprint(
    val index: Int,
    val nameRes: Int,
    val row: Int,
    val col: Int,
    val jobType: JobType,
    val isVault: Boolean,
    val isFence: Boolean,
    val riskLow: Int,
    val riskHigh: Int
)

data class District(
    val index: Int,
    val nameRes: Int,
    val row: Int,
    val col: Int,
    val jobType: JobType,
    val isVault: Boolean,
    val isFence: Boolean,
    val risk: Int,
    val payout: Int,
    val missionId: Int,
    val decoded: Boolean = false,
    val visited: Boolean = false,
    val lockedByPatrol: Boolean = false,
    val patrolTarget: Boolean = false
)

data class CrewMember(
    val id: Int,
    val nameRes: Int,
    val jobType: JobType,
    val nerve: Int
)

data class MissionChoice(
    val captionRes: Int,
    val takeDelta: Int,
    val heatDelta: Int,
    val repDelta: Int,
    val pinId: Int
)

data class Mission(
    val id: Int,
    val titleRes: Int,
    val bodyRes: Int,
    val choiceA: MissionChoice,
    val choiceB: MissionChoice
)

data class Upgrade(
    val id: Int,
    val nameRes: Int,
    val effectRes: Int,
    val cost: Int
)

data class Dossier(
    val id: Int,
    val lineRes: Int,
    val hintRes: Int,
    val linkedId: Int
)

data class EndingInfo(
    val id: Int,
    val titleRes: Int,
    val textRes: Int
)

data class NightRun(
    val seed: Long,
    val districts: List<District>,
    val crew: List<CrewMember>,
    val currentIndex: Int,
    val movesLeft: Int,
    val movesTotal: Int,
    val heat: Int,
    val heatCap: Int,
    val take: Int,
    val banked: Int,
    val streakX10: Int,
    val bestStreakX10: Int,
    val decodeCharges: Int,
    val districtsHit: Int,
    val repDelta: Int,
    val newPins: List<Int>
)

data class JobResolution(
    val outcome: JobOutcome,
    val gain: Int,
    val heatGain: Int,
    val streakX10: Int,
    val pinId: Int
)

data class NightResult(
    val endingId: Int,
    val banked: Int,
    val lostTake: Int,
    val heatAtExit: Int,
    val districtsHit: Int,
    val bestStreakX10: Int,
    val newPins: List<Int>,
    val repDelta: Int
)
