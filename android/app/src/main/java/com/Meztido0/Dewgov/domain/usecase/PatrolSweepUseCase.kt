package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.District
import kotlin.random.Random

class PatrolSweepUseCase {

    fun lockCount(heat: Int): Int = when {
        heat >= GameConfig.PATROL_HEAT_TWO -> 2
        heat >= GameConfig.PATROL_HEAT_ONE -> 1
        else -> 0
    }

    operator fun invoke(
        districts: List<District>,
        currentIndex: Int,
        heat: Int,
        rng: Random
    ): List<District> {
        val count = lockCount(heat)
        val candidates = GenerateNightUseCase.neighboursOf(currentIndex)
            .filter { !districts[it].isVault }
        val locked = if (count == 0 || candidates.isEmpty()) {
            emptyList()
        } else {
            candidates.shuffled(rng).take(count.coerceAtMost(candidates.size - 1).coerceAtLeast(0))
        }
        val nextTarget = candidates.firstOrNull { it !in locked } ?: -1
        return districts.map { d ->
            d.copy(
                lockedByPatrol = d.index in locked,
                patrolTarget = d.index == nextTarget && count > 0
            )
        }
    }
}
