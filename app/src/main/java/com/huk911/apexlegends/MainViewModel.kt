package com.huk911.apexlegends

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.huk911.apexlegends.models.Floor
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.storage.GameStorage

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = GameStorage(application)
    val inventory = Inventory()
    val floor = Floor()

    init {
        inventory.health = storage.loadHealth(inventory.health)
        inventory.materials = storage.loadMaterials(inventory.materials)
        inventory.knockdownCounter = storage.loadKnockdowns(inventory.knockdownCounter)
    }

    fun saveGame() {
        storage.saveHealth(inventory.health)
        storage.saveMaterials(inventory.materials)
        storage.saveKnockCount(inventory.knockdownCounter)
    }
}