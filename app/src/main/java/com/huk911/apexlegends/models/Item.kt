package com.huk911.apexlegends.models

import com.huk911.apexlegends.Rarity

open class Item (val name: String, val rarity: Rarity) {
    val isGold: Boolean
        get() = rarity == Rarity.LEGENDARY

    open fun calculateValue(): Int {
        return 10
    }
    override fun toString() = name + " (" + rarity + ")"
}