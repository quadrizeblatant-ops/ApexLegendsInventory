package com.huk911.apexlegends

import android.content.res.ColorStateList
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

    private val inventory = Inventory()
    private val floor = Floor()

    private lateinit var itemCard: TextView
    private lateinit var backpackCountCard: TextView
    private lateinit var nextItemButton: Button
    private lateinit var equipButton: Button
    private lateinit var useButton: Button
    private lateinit var dropButton: Button

    private lateinit var handCard: TextView
    private lateinit var secHandCard: TextView
    private lateinit var dropHandButton: Button
    private lateinit var dropSecHandButton: Button
    private lateinit var swapButton: ImageButton

    private lateinit var floorCard: TextView
    private lateinit var floorCountCard: TextView
    private lateinit var nextFloorItemButton: Button
    private lateinit var pickButton: Button

    private lateinit var healthBarCard: TextView
    private lateinit var healthProgress: ProgressBar
    private lateinit var infoCard: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        itemCard = findViewById(R.id.itemCard)
        backpackCountCard = findViewById(R.id.tv_backpack_count)
        nextItemButton = findViewById(R.id.btn_next_item)
        equipButton = findViewById(R.id.btn_equip)
        useButton = findViewById(R.id.btn_use)
        dropButton = findViewById(R.id.btn_drop)
        handCard = findViewById(R.id.tv_main_hand_card)
        secHandCard = findViewById(R.id.tv_sec_hand_card)
        dropHandButton = findViewById(R.id.btn_drop_from_hand)
        dropSecHandButton = findViewById(R.id.btn_drop_from_sec_hand)
        swapButton = findViewById(R.id.btn_swap)
        floorCard = findViewById(R.id.tv_floor_card)
        floorCountCard = findViewById(R.id.tv_floor_count)
        nextFloorItemButton = findViewById(R.id.btn_next_floor_item)
        pickButton = findViewById(R.id.btn_pickup)
        healthBarCard = findViewById(R.id.tv_health_bar)
        healthProgress = findViewById(R.id.progress_health)
        infoCard = findViewById(R.id.tv_hp_info)

        dropHandButton.setOnClickListener {
            val droppedWeapon = inventory.dropPrimary()
            if (droppedWeapon != null) {
                floor.putDown(droppedWeapon)
                showInfo("Дропнут: " + droppedWeapon)
            }
            renderHands()
            renderFloor()
        }

        dropSecHandButton.setOnClickListener {
            val droppedWeapon = inventory.dropSecondary()
            if (droppedWeapon != null) {
                floor.putDown(droppedWeapon)
                showInfo("Дропнут: " + droppedWeapon)
            }
            renderHands()
            renderFloor()
        }

        equipButton.setOnClickListener {
            val isEquipped = inventory.equipShownWeapon()
            if (isEquipped) {
                showInfo("Эквипнуто: " + inventory.primaryWeapon)
            } else {
                showInfo("В руки можно взять только оружие")
            }
            renderHands()
            renderBackpack()
        }

        swapButton.setOnClickListener {
            inventory.swapHands()
            showInfo("Оружие в руках поменялось местами")
            renderHands()
        }

        nextItemButton.setOnClickListener {
            inventory.moveToNextItem()
            renderBackpack()
        }

        nextFloorItemButton.setOnClickListener {
            floor.moveToNextItem()
            renderFloor()
        }

        pickButton.setOnClickListener {
            val takenItem = floor.takeShownItem()
            if (takenItem != null) {
                inventory.pickUp(takenItem)
                showInfo("Подобран: " + takenItem)
                val totalValue = inventory.calculateTotalValue()
                Log.d("Inventory", "Полная ценность инвентаря: " + totalValue)
            }
            renderFloor()
            renderBackpack()
        }

        dropButton.setOnClickListener {
            val droppedItem = inventory.dropShownItem()
            if (droppedItem != null) {
                floor.putDown(droppedItem)
                showInfo("Выброшен: " + droppedItem)
            }
            renderBackpack()
            renderFloor()
        }

        useButton.setOnClickListener {
            val useMessage = inventory.useShownItem()
            showInfo(useMessage)
            renderBackpack()
            renderHealth()
        }

        renderScreen()   // первый кадр: экран рисуется из модели ещё до кликов
    }

    private fun renderScreen() {
        renderHands()
        renderBackpack()
        renderFloor()
        renderHealth()
    }

    private fun renderHands() {
        handCard.text = inventory.primaryWeapon?.toString() ?: "В руках пусто"
        secHandCard.text = inventory.secondaryWeapon?.toString() ?: "Во втором слоте пусто"
        dropHandButton.isEnabled = inventory.primaryWeapon != null
        dropSecHandButton.isEnabled = inventory.secondaryWeapon != null
        swapButton.isEnabled = inventory.primaryWeapon != null || inventory.secondaryWeapon != null
    }

    private fun renderBackpack() {
        val isBackpackEmpty = inventory.backpack.isEmpty()
        backpackCountCard.text = "Предметов: " + inventory.backpack.size
        val shownItem = inventory.shownItem
        if (shownItem != null) {
            itemCard.text = shownItem.toString()
            itemCard.setTextColor(pickRarityColor(shownItem.rarity))
        } else {
            itemCard.text = "Рюкзак пуст"
        }
        nextItemButton.isEnabled = !isBackpackEmpty
        equipButton.isEnabled = !isBackpackEmpty
        useButton.isEnabled = !isBackpackEmpty
        dropButton.isEnabled = !isBackpackEmpty
    }

    private fun renderFloor() {
        val isFloorEmpty = floor.items.isEmpty()
        floorCountCard.text = "На полу: " + floor.items.size
        val shownItem = floor.shownItem
        if (shownItem != null) {
            floorCard.text = shownItem.toString()
            floorCard.setTextColor(pickRarityColor(shownItem.rarity))
        } else {
            floorCard.text = "На полу пусто"
        }
        nextFloorItemButton.isEnabled = !isFloorEmpty
        pickButton.isEnabled = !isFloorEmpty
    }

    private fun renderHealth() {
        healthBarCard.text = "HP: " + inventory.health
        healthProgress.progress = inventory.health
        val healthColor = pickHealthColor(inventory.health)
        healthBarCard.setTextColor(healthColor)
        healthProgress.progressTintList = ColorStateList.valueOf(healthColor)
    }

    private fun showInfo(message: String) {
        infoCard.text = message
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

    fun pickHealthColor(health: Int): Int = when {
        health >= 70 -> Color.GREEN
        health >= 30 -> Color.YELLOW
        else -> Color.RED
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
