package com.huk911.apexlegends.models

open class Item (val name: String, val rarity: String) {
    val isGold: Boolean
        get() = rarity == "Legendary"

    open fun calculateValue(): Int {
        return 10
    }
    override fun toString() = name + " (" + rarity + ")"
}