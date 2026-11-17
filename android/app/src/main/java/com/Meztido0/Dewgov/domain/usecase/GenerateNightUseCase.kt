package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.District
import com.Meztido0.Dewgov.domain.model.DistrictBlueprint
import com.Meztido0.Dewgov.domain.model.NightRun
import kotlin.random.Random

data class NightSetup(
    val nightIndex: Int,
    val moves: Int,
    val heatCap: Int,
    val decodeCharges: Int,
    val banked: Int,
    val missionIds: List<Int>
)

class GenerateNightUseCase {

    operator fun invoke(
        blueprints: List<DistrictBlueprint>,
        crewTemplate: List<CrewMember>,
        setup: NightSetup
    ): NightRun {
        var attempt = 0
        var districts = emptyList<District>()
        var seed = 0L
        while (attempt < GameConfig.SOLVE_ATTEMPTS) {
            seed = seedFor(setup.nightIndex, attempt)
            val candidate = build(blueprints, setup, Random(seed))
            if (isSolvable(candidate, setup.moves)) {
                districts = candidate
                break
            }
            attempt++
        }
        if (districts.isEmpty()) {
            seed = seedFor(setup.nightIndex, GameConfig.SOLVE_ATTEMPTS)
            districts = fallback(blueprints, setup)
        }
        return NightRun(
            seed = seed % 100000L,
            districts = districts,
            crew = crewTemplate.map { it.copy(nerve = GameConfig.NERVE_MAX) },
            currentIndex = GameConfig.START_INDEX,
            movesLeft = setup.moves,
            movesTotal = setup.moves,
            heat = 0,
            heatCap = setup.heatCap,
            take = 0,
            banked = 0,
            streakX10 = GameConfig.STREAK_MIN_X10,
            bestStreakX10 = GameConfig.STREAK_MIN_X10,
            decodeCharges = setup.decodeCharges,
            districtsHit = 0,
            repDelta = 0,
            newPins = emptyList()
        )
    }

    private fun seedFor(nightIndex: Int, attempt: Int): Long {
        val base = (nightIndex + 1).toLong() * GameConfig.SEED_A
        val salt = (attempt + 1).toLong() * GameConfig.SEED_B
        return (base xor salt) and 0x7FFFFFFFL
    }

    private fun build(
        blueprints: List<DistrictBlueprint>,
        setup: NightSetup,
        rng: Random
    ): List<District> {
        val open = blueprints.filter { !it.isVault && it.index != GameConfig.START_INDEX }.map { it.index }
        val missionSlots = open.shuffled(rng).take(setup.missionIds.size)
        val fenceSlots = open.filter { it !in missionSlots }.shuffled(rng).take(2)
        return blueprints.map { bp ->
            val risk = if (bp.isVault) 5 else rng.nextInt(bp.riskLow, bp.riskHigh + 1)
            val spread = rng.nextInt(-120, 121)
            val payout = if (bp.isVault) 2400 + rng.nextInt(0, 601) else 400 + risk * 180 + spread
            val slot = missionSlots.indexOf(bp.index)
            District(
                index = bp.index,
                nameRes = bp.nameRes,
                row = bp.row,
                col = bp.col,
                jobType = bp.jobType,
                isVault = bp.isVault,
                isFence = bp.index in fenceSlots,
                risk = risk,
                payout = payout,
                missionId = if (slot >= 0) setup.missionIds[slot] else -1
            )
        }
    }

    private fun fallback(blueprints: List<DistrictBlueprint>, setup: NightSetup): List<District> {
        val risks = intArrayOf(2, 3, 2, 4, 5, 3, 2, 1, 4)
        val missionAt = intArrayOf(0, 1, 5)
        val fenceAt = intArrayOf(6, 8)
        return blueprints.map { bp ->
            val risk = risks[bp.index]
            val slot = missionAt.indexOf(bp.index)
            District(
                index = bp.index,
                nameRes = bp.nameRes,
                row = bp.row,
                col = bp.col,
                jobType = bp.jobType,
                isVault = bp.isVault,
                isFence = bp.index in fenceAt.toList(),
                risk = risk,
                payout = if (bp.isVault) 2600 else 400 + risk * 180,
                missionId = if (slot >= 0 && slot < setup.missionIds.size) setup.missionIds[slot] else -1
            )
        }
    }

    private fun isSolvable(districts: List<District>, moves: Int): Boolean {
        val startNeighbours = neighboursOf(GameConfig.START_INDEX)
        if (startNeighbours.none { districts[it].risk <= 3 && !districts[it].isVault }) return false
        if (!reachesVaultViaFence(districts, moves)) return false
        val earnable = districts
            .filter { !it.isVault }
            .map { it.payout }
            .sortedDescending()
            .take(moves - 1)
            .sum()
        return earnable >= GameConfig.MIN_ROUTE_PAYOUT
    }

    private fun reachesVaultViaFence(districts: List<District>, moves: Int): Boolean {
        val seen = HashSet<Int>()
        var frontier = listOf(GameConfig.START_INDEX to false)
        var step = 0
        while (step < moves) {
            val next = ArrayList<Pair<Int, Boolean>>()
            for ((node, fence) in frontier) {
                for (n in neighboursOf(node)) {
                    val fenceNow = fence || districts[n].isFence
                    if (n == GameConfig.VAULT_INDEX && fenceNow) return true
                    val key = n * 2 + if (fenceNow) 1 else 0
                    if (seen.add(key)) next.add(n to fenceNow)
                }
            }
            if (next.isEmpty()) return false
            frontier = next
            step++
        }
        return false
    }

    companion object {
        fun neighboursOf(index: Int): List<Int> {
            val size = GameConfig.GRID_SIZE
            val row = index / size
            val col = index % size
            val out = ArrayList<Int>(4)
            if (row > 0) out.add(index - size)
            if (row < size - 1) out.add(index + size)
            if (col > 0) out.add(index - 1)
            if (col < size - 1) out.add(index + 1)
            return out
        }
    }
}
