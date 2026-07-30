package com.huk911.apexlegends

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)

        val r301 = Weapon("R-301", "Rare", 14, 18)
        val syringe = Consumable("Syringe", "Common", 25)
        val arcStar = Grenade("Arc Star", "Rare", 75)
        val kraber = Weapon("Kraber", "Legendary", 150, 6)
        val medKit = Consumable("Med Kit", "Rare", 100)

        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val hpInfoCard = findViewById<TextView>(R.id.tv_hp_info)
        val useButton = findViewById<Button>(R.id.btn_use)
        val switchWeaponButton = findViewById<Button>(R.id.switchWeaponButton)
        val pickButton = findViewById<Button>(R.id.btn_pickup)
        val dropButton = findViewById<Button>(R.id.btn_drop)
        val backpack: MutableList<Item> = mutableListOf(r301, syringe, arcStar, kraber, medKit)

        val firstItem = backpack[0]
        val itemCount = backpack.size


        var health = 42
        healthBarCard.text = "HP: $health"
        var shownSlotNumber = 0

        var currentItem: Item = backpack[0]

        nextItemButton.setOnClickListener {
            if (backpack.isEmpty()) {
                itemCard.text = "Инвентарь пуст"
            } else {
                shownSlotNumber += 1
                if (shownSlotNumber > backpack.size - 1) {
                    shownSlotNumber = 0
                }
                val shownItem = backpack[shownSlotNumber]
                itemCard.text = shownItem.toString()
                Log.i("govno", "тотал хп $health")
                currentItem = shownItem
            }
        }

        useButton.setOnClickListener {
            if (backpack.isEmpty()){
                hpInfoCard.text = "Нечего использовать"
            } else {
                val capturedItem = currentItem
                when (capturedItem) {
                    is Consumable -> {
                        if (health + capturedItem.healAmount < 100) {
                            health += capturedItem.healAmount
                            setHealthValue(healthBarCard, health)
                            val healCard =
                                capturedItem.name + ": +" + capturedItem.healAmount + " HP"
                            hpInfoCard.text = healCard
                        } else {
                            health = 100
                            healthBarCard.text = "HP: 100, вы не можете лечиться"
                        }
                    }

                    is Grenade -> {
                        health -= capturedItem.blastDamage
                        setHealthValue(healthBarCard, health)
                        hpInfoCard.text =
                            "Вы подвзворвались на " + capturedItem.blastDamage + " урона"
                    }
                }
                Log.i("govno", "тотал хп $health")
            }
        }

        switchWeaponButton.setOnClickListener {
            if (backpack.isEmpty()) {
                itemCard.text = "Инвентарь пуст"
            } else {
                var weaponNumber = 0
                weaponNumber++
                if (weaponNumber == 3) weaponNumber = 0
                val current = when (weaponNumber) {
                    0 -> backpack[0]
                    1 -> backpack[1]
                    else -> 0
                }
                val card = current.toString()
                itemCard.text = card
                Log.i("govno", "button clicked with number $weaponNumber")
            }
        }


        pickButton.setOnClickListener {
            val shieldCell = Consumable("Shield Cell", "Common", 25)
            backpack.add(shieldCell)
            val itemCount = backpack.size
            itemCard.text = "Подобрано " + shieldCell.name + ". Предметов в инвентаре: " + itemCount
        }

        dropButton.setOnClickListener {
            if (backpack.isEmpty()) {
                itemCard.text = "Инвентарь пуст"
            } else {
                var itemToDrop = backpack[shownSlotNumber]
                backpack.removeAt(shownSlotNumber)
                itemCard.text = "Предмет " + itemToDrop.toString() + " Выброшен нахуй"
                if (shownSlotNumber > 0) {
                    shownSlotNumber -= 1
                }
            }

        }

    }

    private fun setHealthValue(healthBarCard: TextView, health: Int) {
        healthBarCard.text = "HP: " + health
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

    private fun applyInsets() {
        val rootView = findViewById<View>(R.id.main)
        val basePaddingLeft = rootView.paddingLeft
        val basePaddingTop = rootView.paddingTop
        val basePaddingRight = rootView.paddingRight
        val basePaddingBottom = rootView.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                basePaddingLeft + systemBars.left,
                basePaddingTop + systemBars.top,
                basePaddingRight + systemBars.right,
                basePaddingBottom + systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }
}
