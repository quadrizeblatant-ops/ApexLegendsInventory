package com.huk911.apexlegends.models

import com.huk911.apexlegends.models.Weapon

class Floor {

    val items: MutableList<Item> = mutableListOf(
        Weapon("Flatline", Rarity.COMMON, 16, 20),
        Consumable("Shield Battery", Rarity.RARE, 100),
        Grenade("Thermite", Rarity.RARE, 90),
        Weapon("R-301", Rarity.RARE, 14, 18),
        Consumable("Syringe", Rarity.COMMON, 25),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Weapon("Hemlok", Rarity.RARE, 17, 16),
        Weapon("R-99", Rarity.COMMON, 11, 18),
        Consumable("Med Kit", Rarity.RARE, 100),
        Weapon("Kraber", Rarity.LEGENDARY, 150, 6)
    )

    private var shownSlotNumber = 0

    val shownItem: Item?
        get() = if (items.isEmpty()) {
            null
        } else {
            items[shownSlotNumber]
        }

    fun moveToNextItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        shownSlotNumber += 1
        if (shownSlotNumber > items.size - 1) {
            shownSlotNumber = 0
        }
        return items[shownSlotNumber]
    }

    fun takeShownItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        val shownItem = items[shownSlotNumber]
        items.removeAt(shownSlotNumber)
        if (shownSlotNumber > items.size - 1) {
            shownSlotNumber = 0
        }
        return shownItem
    }

    fun addItem(item: Item) {
        items.add(item)
    }

    fun selectSlot(slotNumber: Int) {
        if (slotNumber < 0 || slotNumber > items.size - 1) {
            return
        }
        shownSlotNumber = slotNumber

    }
}
