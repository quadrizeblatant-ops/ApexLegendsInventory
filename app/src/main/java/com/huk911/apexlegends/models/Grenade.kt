package com.huk911.apexlegends.models

class Grenade(
    name: String,
    rarity: Rarity,
    val blastDamage: Int
) : Item(name, rarity){
    override fun toString(): String {
        val grenadeCard = super.toString()
        return grenadeCard + " Урон от взрыва: " + blastDamage
    }
}