package com.huk911.apexlegends.models

class Weapon(
    name: String,
    rarity: String,
    val damage: Int,
    val magSize: Int
) : Item(name, rarity) {
    fun getCard(): String {
        val deadlyText = if (isDeadly) {
            "(Deadly)"
        } else {
            ""
        }
        return "$name ($rarity), Урон: $damage, $deadlyText Магазин: $magSize"
    }

    override fun calculateValue(): Int {
        val baseValue = super.calculateValue()
        return baseValue + damage
    }

    override fun toString(): String {
        val baseCard = super.toString()
        return baseCard + ", урон " + damage + ", магазин " + magSize
    }

    val isDeadly: Boolean
        get() = damage >= 100
}