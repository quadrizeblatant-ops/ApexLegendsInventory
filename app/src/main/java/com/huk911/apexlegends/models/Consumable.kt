package com.huk911.apexlegends.models

class Consumable(
    name: String,
    rarity: String,
    val healAmount: Int,
) : Item(name, rarity){

    override fun toString(): String {
        val consCard = super.toString()
        return consCard + " Кол-во хила: " + healAmount
    }

}