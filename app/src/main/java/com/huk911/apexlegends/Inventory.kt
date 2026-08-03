package com.huk911.apexlegends

import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

class Inventory {

    val backpack: MutableList<Item> = mutableListOf(
        Weapon("R-301", Rarity.RARE, 14, 18),
        Consumable("Syringe", Rarity.COMMON, 25),
        Grenade("Arc Star", Rarity.RARE, 75),
        Weapon("Kraber", Rarity.LEGENDARY, 150, 6),
        Consumable("Med Kit", Rarity.RARE, 100)
    )

    var primaryWeapon: Weapon? = null
    var secondaryWeapon: Weapon? = null
    var health = 42
    private var shownSlotNumber = 0

    val shownItem: Item?
        get() = if (backpack.isEmpty()) null else backpack[shownSlotNumber]

    fun moveToNextItem(): Item? {
        if (backpack.isEmpty()) {
            return null
        }
        shownSlotNumber = shownSlotNumber + 1
        if (shownSlotNumber > backpack.size - 1) {
            shownSlotNumber = 0
        }
        return backpack[shownSlotNumber]
    }

    fun pickUp(newItem: Item) {
        backpack.add(newItem)
    }

    fun dropShownItem(): Item? {
        if (backpack.isEmpty()) {
            return null
        }
        val droppedItem = backpack[shownSlotNumber]
        backpack.removeAt(shownSlotNumber)
        if (shownSlotNumber > backpack.size - 1) {
            shownSlotNumber = 0
        }
        return droppedItem
    }

    fun useShownItem(): String {
        if (backpack.isEmpty()) {
            return "Нечего использовать"
        }
        val chosenItem = backpack[shownSlotNumber]
        return when (chosenItem) {
            is Consumable -> {
                if (health >= 100) {
                    "HP уже 100, лечиться незачем"          // неудача — предмет не тратится
                } else {
                    health = health + chosenItem.healAmount
                    if (health > 100) {
                        health = 100
                    }
                    removeUsedItem()
                    chosenItem.name + ": +" + chosenItem.healAmount + " HP"
                }
            }

            is Grenade -> {
                health = health - chosenItem.blastDamage
                removeUsedItem()
                "Вы подорвались на " + chosenItem.blastDamage + " урона"
            }

            else -> "Это нельзя использовать"
        }
    }

    private fun removeUsedItem() {
        backpack.removeAt(shownSlotNumber)
        if (shownSlotNumber > backpack.size - 1) {
            shownSlotNumber = 0
        }
    }

    fun equipShownWeapon(): Boolean {
        if (backpack.isEmpty()) {
            return false
        }
        val chosenItem = backpack[shownSlotNumber]
        if (chosenItem is Weapon) {
            backpack.removeAt(shownSlotNumber)
            val previousWeapon = primaryWeapon
            if (previousWeapon != null) {
                backpack.add(previousWeapon)
            }
            primaryWeapon = chosenItem
            if (shownSlotNumber > backpack.size - 1) {
                shownSlotNumber = 0
            }
            return true
        }
        return false
    }

    fun dropPrimary(): Weapon? {
        val droppedWeapon = primaryWeapon   // снимок: вернём бывшего жильца руки
        primaryWeapon = null
        return droppedWeapon
    }

    fun dropSecondary(): Weapon? {
        val droppedWeapon = secondaryWeapon
        secondaryWeapon = null
        return droppedWeapon
    }

    fun swapHands() {
        val temporaryWeapon = primaryWeapon
        primaryWeapon = secondaryWeapon
        secondaryWeapon = temporaryWeapon
    }

    fun calculateTotalValue(): Int {
        var totalValue = 0
        for (item in backpack) {
            totalValue = totalValue + item.calculateValue()
        }
        val primaryValue = primaryWeapon?.calculateValue() ?: 0
        val secondaryValue = secondaryWeapon?.calculateValue() ?: 0
        return totalValue + primaryValue + secondaryValue
    }
}
