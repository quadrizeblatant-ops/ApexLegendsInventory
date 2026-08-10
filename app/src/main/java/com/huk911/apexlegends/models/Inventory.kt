package com.huk911.apexlegends.models

class Inventory {

    val shownItem: Item?
        get() = if (backpack.isEmpty()) {
            null
        } else {
            backpack[currentSelectedIndex]
        }

    val backpack: MutableList<Item> = mutableListOf(
        Consumable("Syringe", Rarity.COMMON, 25),
    )
    var primaryWeapon: Weapon? = null
    var secondaryWeapon: Weapon? = null
    var health = 42
    private var currentSelectedIndex = 0

    fun moveToNextItem(): Item? {
        if (backpack.isEmpty()) {
            return null
        }
        currentSelectedIndex += 1
        if (currentSelectedIndex > backpack.size - 1) {
            currentSelectedIndex = 0
        }
        return backpack[currentSelectedIndex]
    }

    fun pickUp(newItem: Item) {
        backpack.add(newItem)
    }

    fun useShownItem(): Item? {
        val shownItem = backpack[currentSelectedIndex]
        when (shownItem) {
            is Consumable -> {
                if(health >= 100) {
                    health = 100
                    return null
                } else {
                    val newHealth = health + shownItem.healAmount
                    if (newHealth > 100) {
                        health = 100
                    } else {
                        health = newHealth
                    }
                    backpack.removeAt(currentSelectedIndex)
                    keepSlotInBounds()
                    return shownItem
                }
            }
            is Grenade -> {
                health -= shownItem.blastDamage
                backpack.removeAt(currentSelectedIndex)
                keepSlotInBounds()
                return shownItem
            }

            else -> return null
        }
    }

    private fun keepSlotInBounds() {
        if (currentSelectedIndex > backpack.size - 1) {
            currentSelectedIndex = 0
        }

    }

    fun swapWeapon() {
        val previousWeapon = primaryWeapon
        primaryWeapon = secondaryWeapon
        secondaryWeapon = previousWeapon
    }

    fun dropPrimaryWeapon(): Item? {
        if (primaryWeapon == null) {
            return null
        } else {
            val previousWeapon = primaryWeapon
            primaryWeapon = null
            return previousWeapon
        }
    }

    fun dropSecondaryWeapon(): Item? {
        if (secondaryWeapon == null) {
            return null
        } else {
            val previousWeapon = secondaryWeapon
            secondaryWeapon = null
            return previousWeapon
        }
    }

    fun equipSelectedWeapon(): Weapon? {
        val shownItem = backpack[currentSelectedIndex]
        if (shownItem is Weapon) {
            backpack.removeAt(currentSelectedIndex)
            val previousWeapon = primaryWeapon
            if (previousWeapon != null) {
                backpack.add(previousWeapon)
            }
            primaryWeapon = shownItem
            keepSlotInBounds()
            return shownItem
        }
        return null
    }

    fun equipSecondSelectedWeapon(): Weapon? {
        val shownItem = backpack[currentSelectedIndex]
        if (shownItem is Weapon) {
            backpack.removeAt(currentSelectedIndex)
            val previousWeapon = secondaryWeapon
            if (previousWeapon != null) {
                backpack.add(previousWeapon)
            }
            secondaryWeapon = shownItem
            keepSlotInBounds()
            return shownItem
        }
        return null
    }
    fun dropItemFromBackpack(): Item? {
        if (backpack.isEmpty()) {
            return null
        }
        val removedItem = backpack.removeAt(currentSelectedIndex)
        keepSlotInBounds()
        return removedItem
    }

    fun selectSlot(slotNumber: Int) {
        if (slotNumber < 0 || slotNumber > backpack.size - 1) {
            return
        }
        currentSelectedIndex = slotNumber
    }


}