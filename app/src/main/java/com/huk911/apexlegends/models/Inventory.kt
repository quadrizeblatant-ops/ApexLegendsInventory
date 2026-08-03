package com.huk911.apexlegends.models

import com.huk911.apexlegends.models.Weapon
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade

class Inventory {
    val backpack: MutableList<Item> = mutableListOf(
        Weapon("R-301", Rarity.RARE, 14, 18),
        Consumable("Syringe", Rarity.COMMON, 25),
        Grenade("Arc Star", Rarity.RARE, 75),
        Weapon("Kraber", Rarity.LEGENDARY, 150, 6),
        Consumable("Med Kit", Rarity.RARE, 100),
        Weapon("Peacekeeper", Rarity.RARE, 100, 5)
    )
    var primaryWeapon: Weapon? = null
    var secondaryWeapon: Weapon? = null
    var health = 75
    var shownSlotNumber = 0
}