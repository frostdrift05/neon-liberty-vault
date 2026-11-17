package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.domain.repository.EvidenceRepository
import com.Meztido0.Dewgov.domain.repository.UpgradeRepository

class UpgradeRepositoryImpl(private val store: PreferencesStore) : UpgradeRepository {

    override fun owned(): Set<Int> = store.getIds(PreferencesStore.KEY_UPGRADES)

    override fun install(upgradeId: Int) {
        store.putIds(PreferencesStore.KEY_UPGRADES, owned() + upgradeId)
    }
}

class EvidenceRepositoryImpl(private val store: PreferencesStore) : EvidenceRepository {

    override fun pins(): Set<Int> = store.getIds(PreferencesStore.KEY_PINS)

    override fun unlock(pinIds: List<Int>) {
        if (pinIds.isEmpty()) return
        store.putIds(PreferencesStore.KEY_PINS, pins() + pinIds)
    }
}
