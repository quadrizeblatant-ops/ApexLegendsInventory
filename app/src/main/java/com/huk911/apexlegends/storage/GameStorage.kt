package com.huk911.apexlegends.storage

import android.content.Context

const val PREFS_NAME = "game"
const val KEY_HEALTH = "health"
const val KEY_MATERIALS = "materials"
const val KEY_KNOCKS = "knockdowns"

class GameStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun loadHealth(defaultHealth: Int): Int = prefs.getInt(KEY_HEALTH, defaultHealth)

    fun loadMaterials(defaultMaterials: Int): Int = prefs.getInt(KEY_MATERIALS, defaultMaterials)

    fun loadKnockdowns(defaultKnockdowns: Int): Int = prefs.getInt(KEY_KNOCKS, defaultKnockdowns)




    fun saveHealth(health: Int) {
        val editor = prefs.edit()
        editor.putInt(KEY_HEALTH, health)
        editor.apply()
    }

    fun saveMaterials(materials: Int) {
        val editor = prefs.edit()
        editor.putInt(KEY_MATERIALS, materials)
        editor.apply()
    }

    fun saveKnockCount(knockdownCounter: Int) {
        val editor = prefs.edit()
        editor.putInt(KEY_KNOCKS, knockdownCounter)
        editor.apply()
    }
}
