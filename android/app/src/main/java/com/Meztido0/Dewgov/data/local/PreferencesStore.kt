package com.Meztido0.Dewgov.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun getInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getIds(key: String): Set<Int> {
        val raw = prefs.getString(key, "") ?: ""
        if (raw.isEmpty()) return emptySet()
        return raw.split(SEPARATOR).mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun putIds(key: String, value: Set<Int>) {
        prefs.edit().putString(key, value.sorted().joinToString(SEPARATOR)).apply()
    }

    companion object {
        private const val STORE_NAME = "neon_liberty_vault"
        private const val SEPARATOR = ","

        const val KEY_BANKED = "banked_chips"
        const val KEY_BEST = "best_night"
        const val KEY_NIGHTS = "nights_run"
        const val KEY_REPUTATION = "reputation"
        const val KEY_ENDINGS = "endings_seen"
        const val KEY_VAULT_CODE = "vault_code"
        const val KEY_UPGRADES = "upgrades_owned"
        const val KEY_PINS = "evidence_pins"
    }
}
