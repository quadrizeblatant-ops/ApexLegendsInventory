package com.huk911.apexlegends.models

class Weapon(
    name: String,
    rarity: Rarity,
    val ammoType: AmmoType,
    val damage: Int,
    val magSize: Int,

) : Item(name, rarity), Recyclable {
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
        return baseCard + ", урон " + damage + ", магазин " + magSize + "Тип патронов: "
    }

    val isDeadly: Boolean
        get() = damage >= 100
    override fun calculateScrapMaterials(): Int {
        return when (rarity) {
             Rarity.COMMON -> 10
             Rarity.RARE -> 20
             Rarity.EPIC -> 30
             Rarity.LEGENDARY -> 40
         }
    }
}