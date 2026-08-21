package com.huk911.apexlegends.models

import android.hardware.lights.Light
import android.util.Log
import kotlin.math.log

class Floor {

    val items: MutableList<Item> = mutableListOf(
        Weapon("Flatline", Rarity.COMMON, AmmoType.HEAVY, 20, 18),
        Consumable("Shield Battery", Rarity.RARE, 100),
        Grenade("Thermite", Rarity.RARE, 90),
        Weapon("R-301", Rarity.RARE, AmmoType.LIGHT, 18, 14),
        Consumable("Syringe", Rarity.COMMON, 25),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Grenade("Arc Star", Rarity.RARE, 75),
        Weapon("Hemlok", Rarity.RARE, AmmoType.HEAVY, 16, 17),
        Weapon("R-99", Rarity.COMMON, AmmoType.LIGHT, 18, 11),
        Consumable("Med Kit", Rarity.RARE, 100),
        Weapon("Kraber", Rarity.LEGENDARY, AmmoType.SNIPER, 6, 5)
    )

    private var currentSelectedIndex = 0

    val currentSelectedItem: Item?
        get() = if (items.isEmpty()) {
            null
        } else {
            items[currentSelectedIndex]
        }

    fun moveToNextItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        currentSelectedIndex += 1
        if (currentSelectedIndex > items.size - 1) {
            currentSelectedIndex = 0
        }
        return items[currentSelectedIndex]
    }

    fun takeCurrentSelectedItem(): Item? {
        if (items.isEmpty()) {
            return null
        }
        val shownItem = items[currentSelectedIndex]
        items.removeAt(currentSelectedIndex)
        Log.i("govno", "предмет удалён:  " + shownItem.name)
        Log.i("govno", items.toString())
        if (currentSelectedIndex > items.size - 1) {
            currentSelectedIndex = 0
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
        currentSelectedIndex = slotNumber

    }
}
