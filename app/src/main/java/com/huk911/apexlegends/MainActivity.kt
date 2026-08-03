package com.huk911.apexlegends

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        val inventory = Inventory()
        val floor = Floor()

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)
        val backpackCountCard = findViewById<TextView>(R.id.tv_backpack_count)
        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val healthProgress = findViewById<ProgressBar>(R.id.progress_health)
        val infoCard = findViewById<TextView>(R.id.tv_hp_info)
        val useButton = findViewById<Button>(R.id.btn_use)
        val pickButton = findViewById<Button>(R.id.btn_pickup)
        val dropButton = findViewById<Button>(R.id.btn_drop)
        val handCard = findViewById<TextView>(R.id.tv_main_hand_card)
        val secHandCard = findViewById<TextView>(R.id.tv_sec_hand_card)
        val equipButton = findViewById<Button>(R.id.btn_equip)
        val dropHandButton = findViewById<Button>(R.id.btn_drop_from_hand)
        val dropSecHandButton = findViewById<Button>(R.id.btn_drop_from_sec_hand)
        val swapButton = findViewById<ImageButton>(R.id.btn_swap)
        val floorCard = findViewById<TextView>(R.id.tv_floor_card)
        val floorCountCard = findViewById<TextView>(R.id.tv_floor_count)
        val nextFloorItemButton = findViewById<Button>(R.id.btn_next_floor_item)

        healthBarCard.text = "HP: " + inventory.health
        healthProgress.progress = inventory.health
        handCard.text = inventory.primaryWeapon?.toString() ?: "В руках пусто"
        secHandCard.text = inventory.secondaryWeapon?.toString() ?: "Во втором слоте пусто"
        backpackCountCard.text = "Предметов: " + inventory.backpack.size
        floorCountCard.text = "На полу: " + floor.items.size

        dropHandButton.setOnClickListener {
            val droppedWeapon = inventory.dropPrimary()
            if (droppedWeapon != null) {
                floor.putDown(droppedWeapon)
                handCard.text = "В руках пусто"
                floorCountCard.text = "На полу: " + floor.items.size
            } else {
                handCard.text = "В руках пусто, нечего дропать"
            }
        }

        dropSecHandButton.setOnClickListener {
            val droppedWeapon = inventory.dropSecondary()
            if (droppedWeapon != null) {
                floor.putDown(droppedWeapon)
                secHandCard.text = "Во втором слоте пусто"
                floorCountCard.text = "На полу: " + floor.items.size
            } else {
                secHandCard.text = "Во втором слоте пусто, нечего дропать"
            }
        }

        equipButton.setOnClickListener {
            if (inventory.backpack.isEmpty()) {
                handCard.text = "В рюкзаке пусто, нечего эквипать"
            } else {
                val isEquipped = inventory.equipShownWeapon()
                if (isEquipped) {
                    handCard.text = inventory.primaryWeapon?.toString() ?: "В руках пусто"
                    backpackCountCard.text = "Предметов: " + inventory.backpack.size
                } else {
                    handCard.text = "В руки можно взять только одно оружие"
                }
            }
        }

        swapButton.setOnClickListener {
            inventory.swapHands()
            handCard.text = inventory.primaryWeapon?.toString() ?: "В руках пусто"
            secHandCard.text = inventory.secondaryWeapon?.toString() ?: "Во втором слоте пусто"
        }

        nextItemButton.setOnClickListener {
            val shownItem = inventory.moveToNextItem()
            if (shownItem != null) {
                itemCard.text = shownItem.toString()
                val rarityColor = pickRarityColor(shownItem.rarity)
                itemCard.setTextColor(rarityColor)
            } else {
                itemCard.text = "Инвентарь пуст"
            }
        }

        nextFloorItemButton.setOnClickListener {
            val shownItem = floor.moveToNextItem()
            if (shownItem != null) {
                floorCard.text = shownItem.toString()
                val rarityColor = pickRarityColor(shownItem.rarity)
                floorCard.setTextColor(rarityColor)
            } else {
                floorCard.text = "На полу пусто"
            }
        }

        pickButton.setOnClickListener {
            val takenItem = floor.takeShownItem()
            if (takenItem != null) {
                inventory.pickUp(takenItem)
                floorCard.text = "Подобран: " + takenItem
                backpackCountCard.text = "Предметов: " + inventory.backpack.size
                floorCountCard.text = "На полу: " + floor.items.size
                val totalValue = inventory.calculateTotalValue()
                Log.d("Inventory", "Полная ценность инвентаря: " + totalValue)
            } else {
                floorCard.text = "На полу пусто"
            }
        }

        dropButton.setOnClickListener {
            val droppedItem = inventory.dropShownItem()
            if (droppedItem != null) {
                floor.putDown(droppedItem)
                itemCard.text = "Выброшен: " + droppedItem
                backpackCountCard.text = "Предметов: " + inventory.backpack.size
                floorCountCard.text = "На полу: " + floor.items.size
            } else {
                itemCard.text = "Рюкзак пуст"
            }
        }

        useButton.setOnClickListener {
            val useMessage = inventory.useShownItem()
            infoCard.text = useMessage
            setHealthValue(healthBarCard, inventory.health)
            healthProgress.progress = inventory.health
            backpackCountCard.text = "Предметов: " + inventory.backpack.size
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

    fun pickRarityColor(rarity: Rarity): Int = when (rarity) {
        Rarity.COMMON -> Color.GRAY
        Rarity.RARE -> Color.BLUE
        Rarity.EPIC -> Color.MAGENTA
        Rarity.LEGENDARY -> Color.RED
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
