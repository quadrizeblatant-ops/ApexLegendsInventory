package com.huk911.apexlegends

import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

class Floor {

    val items: MutableList<Item> = mutableListOf(
        Weapon("Flatline", Rarity.COMMON, 16, 20),
        Consumable("Shield Battery", Rarity.RARE, 100),
        Grenade("Thermite", Rarity.RARE, 90)
    )

    private var shownSlotNumber = 0

    fun moveToNextItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        shownSlotNumber = shownSlotNumber + 1
        if (shownSlotNumber > items.size - 1) {
            shownSlotNumber = 0
        }
        return items[shownSlotNumber]
    }

    fun takeShownItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        val takenItem = items[shownSlotNumber]
        items.removeAt(shownSlotNumber)
        if (shownSlotNumber > items.size - 1) {
            shownSlotNumber = 0
        }
        return takenItem
    }

    fun putDown(droppedItem: Item) {
        items.add(droppedItem)
    }
}
