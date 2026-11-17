package com.Meztido0.Dewgov.domain.repository

import com.Meztido0.Dewgov.domain.model.Progress

interface ProgressRepository {
    fun load(): Progress
    fun save(progress: Progress)
}

interface UpgradeRepository {
    fun owned(): Set<Int>
    fun install(upgradeId: Int)
}

interface EvidenceRepository {
    fun pins(): Set<Int>
    fun unlock(pinIds: List<Int>)
}
