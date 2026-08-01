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
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        applyInsets()

        val itemCard: TextView = findViewById(R.id.itemCard)
        val nextItemButton: Button = findViewById(R.id.btn_next_item)

        val r301 = Weapon("R-301", Rarity.RARE, 14, 18)
        val syringe = Consumable("Syringe", Rarity.COMMON, 25)
        val arcStar = Grenade("Arc Star", Rarity.RARE, 75)
        val kraber = Weapon("Kraber", Rarity.LEGENDARY, 150, 6)
        val medKit = Consumable("Med Kit", Rarity.RARE, 100)
        val primary = Weapon("R-99", Rarity.RARE, 11, 18)
        val secondary = Weapon("Wingman", Rarity.EPIC, 45, 6)

        var primaryWeapon: Weapon? = null
        var secondaryWeapon: Weapon? = null

        val healthBarCard = findViewById<TextView>(R.id.tv_health_bar)
        val hpInfoCard = findViewById<TextView>(R.id.tv_hp_info)
        val useButton = findViewById<Button>(R.id.btn_use)
        val switchWeaponButton = findViewById<Button>(R.id.switchWeaponButton)
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
        val eqiupSecButton = findViewById<Button>(R.id.btn_equip_sec_hand)
        val backpack: MutableList<Item> = mutableListOf(r301, syringe, arcStar, kraber, medKit)

        val firstItem = backpack[0]
        val itemCount = backpack.size


        var health = 42
        healthBarCard.text = "HP: $health"
        var shownSlotNumber = 0
        var currentItem: Item = backpack[0]

        val handDescription = primaryWeapon?.toString()               // Рука
        val handText = handDescription ?: "В руках пусто"
        val previousWeapon = primaryWeapon
        handCard.text = handText
        if (previousWeapon != null) {
            backpack.add(previousWeapon)
        }

        dropHandButton.setOnClickListener {
            if (primaryWeapon == null) {
                handCard.text = "В руках пусто, нечего дропать"
            } else {
                primaryWeapon = null
                handCard.text = "В руках пусто"
            }
        }

        dropSecHandButton.setOnClickListener {
            if (secondaryWeapon == null) {
                secHandCard.text = "Во втором слоте пусто, нечего дропать"
            } else {
                secondaryWeapon = null
                secHandCard.text = "Во втором слоте пусто"
            }
        }

        equipButton.setOnClickListener {
            if (backpack.isEmpty()) {
                handCard.text = "В рюкзаке пусто, нечего эквипать"
            } else {
                val chosenItem = backpack[shownSlotNumber]
                if (chosenItem is Weapon) {
                    backpack.removeAt(shownSlotNumber)
                    val previousWeapon = primaryWeapon
                    if (previousWeapon != null) {
                        backpack.add(previousWeapon)
                    }
                    primaryWeapon = chosenItem
                    if (shownSlotNumber > backpack.size - 1) {
                        shownSlotNumber = 0
                    }
                    handCard.text = primaryWeapon?.toString() ?: "В руках пусто"
                } else {
                    handCard.text = "В руки можно взять только одно оружие"
                }
            }
        }

        eqiupSecButton.setOnClickListener {
            if (backpack.isEmpty()) {
                secHandCard.text = "В рюкзаке пусто, нечего эквипать"
            } else {
                val chosenItem = backpack[shownSlotNumber]
                if (chosenItem is Weapon) {
                    backpack.removeAt(shownSlotNumber)
                    val previousWeapon = secondaryWeapon
                    if (previousWeapon != null) {
                        backpack.add(previousWeapon)
                    }
                    secondaryWeapon = chosenItem
                    if (shownSlotNumber > backpack.size - 1) {
                        shownSlotNumber = 0
                    }
                    secHandCard.text = secondaryWeapon?.toString() ?: "Во втором слоте пусто"
                } else {
                    secHandCard.text = "В руки можно взять только одно оружие"
                }
            }
        }

        openInventory.setOnClickListener {
            var backpackText = ""
            var backpackNumber = 0
            for (item in backpack) {
                backpackNumber += 1
                backpackText = backpackText + backpackNumber + ". " + item + "\n"
            }
            val shownText = if (backpack.isEmpty()) "Инвентарь пуст" else backpackText
            inventoryText.text = shownText
        }

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
                val rarityColor = pickRarityColor(shownItem.rarity)
                itemCard.setTextColor(rarityColor)
                Log.i("govno", "тотал хп $health")
                currentItem = shownItem
            }
        }

        useButton.setOnClickListener {
            if (backpack.isEmpty()) {
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
            val shieldCell = Consumable("Shield Cell", Rarity.COMMON, 25)
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

        valueButton.setOnClickListener {
            var backpackValue = 0
            val handValue = primary.calculateValue() + secondary.calculateValue()
            for (item in backpack) {
                backpackValue += item.calculateValue()
            }
            valueCard.text = "Ценность предметов в руках: " + handValue + "\n" +
                    "Ценность инвентаря: " + backpackValue
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
