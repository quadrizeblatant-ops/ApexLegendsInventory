package com.huk911.apexlegends

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)

        val firstSlot: Item = Weapon("R-301", "Rare", 14, 18)
        val secondSlot: Item = Consumable("Syringe", "Common", 25)
        val thirdSlot: Item = Grenade("Arc Star", "Rare", 75)
        val fourthSlot: Item = Weapon("Kraber","Legendary", 150, 6)
        val fifthSlot: Item = Consumable("Med Kit", "Rare", 100)

        var shownSlotNumber = 0

        nextItemButton.setOnClickListener {
            shownSlotNumber += 1
            if(shownSlotNumber > 4) {
                shownSlotNumber = 0
            }
            val shownSlot = when (shownSlotNumber) {
                0 -> firstSlot
                1 -> secondSlot
                2 -> thirdSlot
                3 -> fourthSlot
                else -> fifthSlot
            }
            itemCard.text = shownSlot.toString()
        }

        var health = 42
        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val consumableCard = findViewById<TextView>(R.id.tv_consumable)
        val consUseButton = findViewById<Button>(R.id.btn_consumable_use)
        val switchWeaponButton = findViewById<Button>(R.id.switchWeaponButton)
        val valueCard = findViewById<TextView>(R.id.tv_summaryCard)
        val valueButton = findViewById<Button>(R.id.btn_value_button)

        val primary = Weapon("R-301", "Rare", 14, 18)
        val secondary = Weapon("Peacekeeper", "Epic", 100, 5)
        val kraber = Weapon("Kraber", "Legendary", 140, 4)
        val syringe = Consumable("Syringe", "Common", 25)
        var weaponNumber = 0


        consUseButton.setOnClickListener {
            if (health + syringe.healAmount < 100) {
                health += syringe.healAmount
                healthBarCard.text = "HP: " + health
                val healCard = syringe.name + ": +" + syringe.healAmount + " HP"
                consumableCard.text = healCard
            } else {
                health = 100
                healthBarCard.text = "HP: 100, вы не можете лечиться"
            }
            Log.i("govno", "тотал хп $health")
        }

        switchWeaponButton.setOnClickListener {
            weaponNumber++
            if (weaponNumber == 3) weaponNumber = 0
            val current = when (weaponNumber) {
                0 -> primary
                1 -> secondary
                else -> kraber
            }
            val card = current.toString()
            itemCard.text = card
            Log.i("govno", "button clicked with number $weaponNumber")
        }

        valueButton.setOnClickListener {
            val totalValue = primary.calculateValue() + secondary.calculateValue()
            valueCard.text = "Ценность инвентаря: " + totalValue
        }
    }

    fun calculateTotalDamage(damage: Int, shots: Int): Int {
        return damage * shots
    }

    fun classifyDamage(damage: Int): String {
        val tier = when {
            damage >= 100 -> "Убойный"
            damage >= 40 -> "Мощный"
            else -> "Слабый"
        }
        return tier
    }
}
