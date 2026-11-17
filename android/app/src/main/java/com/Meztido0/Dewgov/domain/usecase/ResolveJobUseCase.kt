package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.District
import com.Meztido0.Dewgov.domain.model.JobOutcome
import com.Meztido0.Dewgov.domain.model.JobResolution
import com.Meztido0.Dewgov.domain.model.Reputation

class ResolveJobUseCase {

    fun successChance(district: District, crew: CrewMember, heat: Int): Int {
        val riskScore = district.risk * 10
        val crewMatch = if (crew.jobType == district.jobType) 18 else 0
        val skillBonus = crew.nerve / 10
        val heatPenalty = heat / 5
        return (55 + crewMatch + skillBonus - riskScore - heatPenalty).coerceIn(10, 92)
    }

    operator fun invoke(
        district: District,
        crew: CrewMember,
        heat: Int,
        streakX10: Int,
        band: Reputation,
        armoredTrunk: Boolean,
        roll: Int
    ): JobResolution {
        val chance = successChance(district, crew, heat)
        val revisit = district.visited
        val outcome = when {
            roll < chance -> JobOutcome.CLEAN
            roll < chance + 18 -> JobOutcome.CLOSE_CALL
            else -> JobOutcome.BURNED
        }
        val basePayout = if (revisit) district.payout / 2 else district.payout
        val repFactor = if (band == Reputation.SYNDICATE) 115 else 100
        val trunkFactor = if (armoredTrunk) 110 else 100
        val gain = when (outcome) {
            JobOutcome.CLEAN -> basePayout * streakX10 / 10 * repFactor / 100 * trunkFactor / 100
            JobOutcome.CLOSE_CALL -> basePayout / 2
            JobOutcome.BURNED -> 0
        }
        val rawHeat = when (outcome) {
            JobOutcome.CLEAN -> district.risk * 2
            JobOutcome.CLOSE_CALL -> district.risk * 6
            JobOutcome.BURNED -> district.risk * 10
        }
        var heatGain = if (revisit) rawHeat * 3 / 2 else rawHeat
        if (band == Reputation.STREET) heatGain = heatGain * 85 / 100
        val nextStreak = when (outcome) {
            JobOutcome.CLEAN -> (streakX10 + GameConfig.STREAK_STEP_X10).coerceAtMost(GameConfig.STREAK_MAX_X10)
            else -> GameConfig.STREAK_MIN_X10
        }
        val pin = if (outcome == JobOutcome.CLEAN) district.index else -1
        return JobResolution(
            outcome = outcome,
            gain = gain,
            heatGain = heatGain,
            streakX10 = nextStreak,
            pinId = pin
        )
    }
}
