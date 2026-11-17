package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.domain.model.MissionChoice
import com.Meztido0.Dewgov.domain.model.NightRun

class ApplyMissionChoiceUseCase {

    operator fun invoke(run: NightRun, choice: MissionChoice): NightRun {
        val pins = if (run.newPins.contains(choice.pinId)) run.newPins else run.newPins + choice.pinId
        return run.copy(
            take = (run.take + choice.takeDelta).coerceAtLeast(0),
            heat = (run.heat + choice.heatDelta).coerceIn(0, run.heatCap),
            repDelta = run.repDelta + choice.repDelta,
            newPins = pins
        )
    }
}
