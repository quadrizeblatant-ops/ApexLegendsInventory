package com.huk911.apexlegends.models

class Grenade(
    name: String,
    rarity: Rarity,
    val blastDamage: Int
) : Item(name, rarity), Recyclable {

    override fun toString(): String {
        val grenadeCard = super.toString()
        return grenadeCard + " Урон от взрыва: " + blastDamage
    }

    override fun calculateScrapMaterials(): Int {
        return 15
    }
}