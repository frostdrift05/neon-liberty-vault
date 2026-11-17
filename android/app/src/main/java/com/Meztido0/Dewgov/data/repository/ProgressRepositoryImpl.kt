package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.domain.model.Progress
import com.Meztido0.Dewgov.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    override fun load(): Progress = Progress(
        bankedChips = store.getInt(PreferencesStore.KEY_BANKED, 0),
        bestNight = store.getInt(PreferencesStore.KEY_BEST, 0),
        nightsRun = store.getInt(PreferencesStore.KEY_NIGHTS, 0),
        reputation = store.getInt(PreferencesStore.KEY_REPUTATION, 0),
        endingsSeen = store.getIds(PreferencesStore.KEY_ENDINGS),
        vaultCodeKnown = store.getBoolean(PreferencesStore.KEY_VAULT_CODE, false)
    )

    override fun save(progress: Progress) {
        store.putInt(PreferencesStore.KEY_BANKED, progress.bankedChips)
        store.putInt(PreferencesStore.KEY_BEST, progress.bestNight)
        store.putInt(PreferencesStore.KEY_NIGHTS, progress.nightsRun)
        store.putInt(PreferencesStore.KEY_REPUTATION, progress.reputation)
        store.putIds(PreferencesStore.KEY_ENDINGS, progress.endingsSeen)
        store.putBoolean(PreferencesStore.KEY_VAULT_CODE, progress.vaultCodeKnown)
    }
}
