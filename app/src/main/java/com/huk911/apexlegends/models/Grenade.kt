package com.huk911.apexlegends.models

import com.huk911.apexlegends.Rarity

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