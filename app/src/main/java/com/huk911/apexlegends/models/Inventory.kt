package com.huk911.apexlegends.models

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

    fun moveToNextItem(): Item? {
        if (backpack.isEmpty()) {
            return null
        }
        shownSlotNumber += 1
        if (shownSlotNumber > backpack.size - 1) {
            shownSlotNumber = 0
        }
        return backpack[shownSlotNumber]
    }

    fun pickUp(newItem: Item) {
        backpack.add(newItem)
    }

    fun useShownItem(): String {
        val shownItem = backpack[shownSlotNumber]
        when (shownItem) {
            is Consumable -> {
                if (health + shownItem.healAmount < 100) {
                    health += shownItem.healAmount
                    val healCard =
                        shownItem.name + ": +" + shownItem.healAmount + " HP"
                    return healCard
                } else {
                    health = 100
                    return "HP: 100, вы не можете лечиться"
                }
            }

            is Grenade -> {
                health -= shownItem.blastDamage
                return "Вы подвзворвались на " + shownItem.blastDamage + " урона"
            }

            else -> return "Это нельзя использовать"
        }
    }

    fun swapWeapon(): String {
        val handDescription = primaryWeapon?.toString()               // Рука
        val handText = handDescription ?: "В руках пусто"
        val previousWeapon = primaryWeapon
        if (previousWeapon != null) {
            backpack.add(previousWeapon)
        }
        return handText
    }

    fun dropPrimaryWeapon(){
        primaryWeapon = null
    }

    fun dropSecondaryWeapon() {
        secondaryWeapon = null
    }

    fun equipSelectedWeapon(): Weapon? {
        val shownItem = backpack[shownSlotNumber]
        if (shownItem is Weapon) {
            backpack.removeAt(shownSlotNumber)
            val previousWeapon = primaryWeapon
            if (previousWeapon != null) {
                backpack.add(previousWeapon)
            }
            primaryWeapon = shownItem
            if (shownSlotNumber > backpack.size - 1) {
                shownSlotNumber = 0
            }
            return shownItem
        }
        return null
    }

    fun equipSecondSelectedWeapon(): String {
        val shownItem = backpack[shownSlotNumber]
        if (shownItem is Weapon) {
            backpack.removeAt(shownSlotNumber)
            val previousWeapon = secondaryWeapon
            if (previousWeapon != null) {
                backpack.add(previousWeapon)
            }
            secondaryWeapon = shownItem
            if (shownSlotNumber > backpack.size - 1) {
                shownSlotNumber = 0
            }
            return primaryWeapon?.toString() ?: "В руках пусто"
        } else {
            return "В руки можно взять только одно оружие"
        }
    }
    fun dropItem(): String {
        if (backpack.isEmpty()) {
            return "Инвентарь пуст"
        } else {
            var itemToDrop = backpack[shownSlotNumber]
            backpack.removeAt(shownSlotNumber)
            return  "Предмет " + itemToDrop.toString() + " Выброшен нахуй"
            if (shownSlotNumber > 0) {
                shownSlotNumber -= 1
            }
        }
    }

}