package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.EndingCause
import com.Meztido0.Dewgov.domain.model.EndingIds
import com.Meztido0.Dewgov.domain.model.NightResult
import com.Meztido0.Dewgov.domain.model.NightRun

class ResolveEndingUseCase {

    operator fun invoke(
        run: NightRun,
        cause: EndingCause,
        reputationAfter: Int,
        vaultCodeKnown: Boolean
    ): NightResult {
        val keepsTake = cause == EndingCause.BREACH || cause == EndingCause.OUT_OF_MOVES
        var banked = if (keepsTake) run.banked + run.take else run.banked
        val lost = if (keepsTake) 0 else run.take
        val ending = when {
            cause == EndingCause.BUST -> EndingIds.BUSTED
            cause == EndingCause.BREACH &&
                banked >= GameConfig.CROWN_TAKE_MIN &&
                reputationAfter >= GameConfig.CROWN_REP_MIN &&
                vaultCodeKnown -> EndingIds.CROWN_TAKE
            cause == EndingCause.BREACH && run.heat < GameConfig.GHOST_HEAT_MAX -> EndingIds.GHOST_EXIT
            reputationAfter <= GameConfig.BURNED_DEAL_REP -> EndingIds.BURNED_DEAL
            else -> EndingIds.EMPTY_VAULT
        }
        if (ending == EndingIds.BURNED_DEAL) {
            banked = banked * (100 - GameConfig.BURNED_DEAL_CUT) / 100
        }
        return NightResult(
            endingId = ending,
            banked = banked,
            lostTake = lost,
            heatAtExit = run.heat,
            districtsHit = run.districtsHit,
            bestStreakX10 = run.bestStreakX10,
            newPins = run.newPins,
            repDelta = run.repDelta
        )
    }
}
