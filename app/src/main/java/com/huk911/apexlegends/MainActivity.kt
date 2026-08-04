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
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.models.Rarity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)

        val inventory = Inventory()

        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val hpInfoCard = findViewById<TextView>(R.id.tv_hp_info)
        val useButton = findViewById<Button>(R.id.btn_use)
        val swapWeaponButton = findViewById<Button>(R.id.switchWeaponButton)
        val pickButton = findViewById<Button>(R.id.btn_pickup)
        val dropButton = findViewById<Button>(R.id.btn_drop)
        val openInventory = findViewById<Button>(R.id.btn_open_inventory)
        val inventoryText = findViewById<TextView>(R.id.tv_full_inventory)
        val valueCard = findViewById<TextView>(R.id.tv_value_text)
        val valueButton = findViewById<Button>(R.id.btn_value)
        val handCard = findViewById<TextView>(R.id.tv_main_hand_card)
        val secHandCard = findViewById<TextView>(R.id.tv_sec_hand_card)
        val equipButton = findViewById<Button>(R.id.btn_equip)
        val dropHandButton = findViewById<Button>(R.id.btn_drop_from_hand)
        val dropSecHandButton = findViewById<Button>(R.id.btn_drop_from_sec_hand)
        val eqiupSecondaryButton = findViewById<Button>(R.id.btn_equip_sec_hand)



        dropHandButton.setOnClickListener {
            if (inventory.primaryWeapon == null) {
                handCard.text = "Дропать нечего, в руках пусто"
            } else {
                inventory.dropPrimaryWeapon()
                handCard.text = "Оружие дропнуто"
            }
        }

        dropSecHandButton.setOnClickListener {
            if (inventory.secondaryWeapon == null) {
                handCard.text = "Дропать нечего, в руках пусто"
            } else {
                inventory.dropSecondaryWeapon()
                handCard.text = "Оружие дропнуто"
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

        openInventory.setOnClickListener {
            var backpackText = ""
            var backpackNumber = 0
            for (item in inventory.backpack) {
                backpackNumber += 1
                backpackText = backpackText + backpackNumber + ". " + item + "\n"
            }
            val shownText = if (inventory.backpack.isEmpty()) "Инвентарь пуст" else backpackText
            inventoryText.text = shownText
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
                hpInfoCard.text = "Нечего использовать"
            } else {
                val resultText = inventory.useShownItem()
                hpInfoCard.text = resultText
            }
        }

        swapWeaponButton.setOnClickListener {
            inventory.swapWeapon()
            handCard.text = inventory.primaryWeapon?.toString() ?: "В руке пусто"
            secHandCard.text = inventory.secondaryWeapon?.toString() ?: "В руке пусто"
        }


        pickButton.setOnClickListener {
            val shieldCell = Consumable("Shield Cell", Rarity.COMMON, 25)
            inventory.pickUp(shieldCell)
            val itemCount = inventory.backpack.size
            itemCard.text = "Подобрано " + shieldCell.name + ". Предметов в инвентаре: " + itemCount
        }

        dropButton.setOnClickListener {
            inventory.dropItem()
            val inventoryIsEmpty = inventory.dropItem()
            itemCard.text = inventoryIsEmpty
            val itemIsDropped = inventory.dropItem()
            itemCard.text = itemIsDropped
        }

        valueButton.setOnClickListener {
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
