package com.huk911.apexlegends

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.huk911.apexlegends.models.Consumable
import android.graphics.Color
import com.huk911.apexlegends.models.Floor
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Rarity
import com.huk911.apexlegends.models.Weapon

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)

        val inventory = Inventory()
        val floor = Floor()

        val infoCard = findViewById<TextView>(R.id.tv_info_card)

        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val useButton = findViewById<Button>(R.id.btn_use)
        val dropButton = findViewById<Button>(R.id.btn_drop)

        val pickButton = findViewById<Button>(R.id.btn_floor_pick)
        val floorInspectButton = findViewById<Button>(R.id.btn_floor_inspect)
        val floorCard = findViewById<TextView>(R.id.tv_floor_card)

        val handCard = findViewById<TextView>(R.id.tv_main_hand_card)
        val secHandCard = findViewById<TextView>(R.id.tv_sec_hand_card)
        val equipButton = findViewById<Button>(R.id.btn_equip)
        val dropHandButton = findViewById<Button>(R.id.btn_drop_from_hand)
        val dropSecHandButton = findViewById<Button>(R.id.btn_drop_from_sec_hand)
        val eqiupSecondaryButton = findViewById<Button>(R.id.btn_equip_sec_hand)



        floorInspectButton.setOnClickListener {
            val shownItem = floor.moveToNextItem()
            if (shownItem != null) {
                floorCard.text = shownItem.toString()
                val rarityColor = pickRarityColor(shownItem.rarity)
                floorCard.setTextColor(rarityColor)
            } else {
                floorCard.text = "Пол пустой"
            }
        }

        dropHandButton.setOnClickListener {
            val droppedItem = inventory.dropPrimaryWeapon()
            if (droppedItem != null) {
                floor.items.add(droppedItem)
                handCard.text = "Оружие выброшено на пол"
            } else {
                handCard.text = "Нечего дропать"
            }
        }

        dropSecHandButton.setOnClickListener {
            val droppedItem = inventory.dropSecondaryWeapon()
            if (droppedItem != null) {
                floor.items.add(droppedItem)
                secHandCard.text = "Оружие выброшено на пол"
            } else {
                secHandCard.text = "Нечего дропать"
            }
        }

        equipButton.setOnClickListener {
            if (inventory.backpack.isEmpty()) {
                handCard.text = "Рюкзак пуст, нечего эквипнуть"
            } else {
                val equippedWeapon = inventory.equipSelectedWeapon()
                handCard.text = equippedWeapon.toString()
            }
        }

        eqiupSecondaryButton.setOnClickListener {
            if (inventory.backpack.isEmpty()) {
                secHandCard.text = "Рюкзак пуст, нечего эквипнуть"
            } else {
                val equippedWeapon = inventory.equipSecondSelectedWeapon()
                secHandCard.text = equippedWeapon.toString()
            }
        }

        nextItemButton.setOnClickListener {
            val shownItem = inventory.moveToNextItem()
            if (shownItem != null) {
                itemCard.text = shownItem.toString()
                val rarityColor = pickRarityColor(shownItem.rarity)
                itemCard.setTextColor(rarityColor)
            } else {
                itemCard.text = "Рюкзак пуст"
            }
        }
        useButton.setOnClickListener {
            if (inventory.backpack.isEmpty()) {
                infoCard.text = "Нечего использовать"
            } else {
                val resultText = inventory.useShownItem()
                infoCard.text = resultText
            }
        }



        pickButton.setOnClickListener {
            val takenItem = floor.takeShownItem()
            if (takenItem != null) {
                inventory.pickUp(takenItem)
                infoCard.text = "Подобран предмет " + takenItem.toString()
            } else {
                infoCard.text = "На полу ничего нет"
            }
        }

        dropButton.setOnClickListener {
            val droppedItem = inventory.dropItem()
            if (droppedItem != null) {
                floor.addItem(droppedItem)
                infoCard.text = droppedItem.toString() + " Выброшен"
            } else {
                infoCard.text = "Нечего выбросить"
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
