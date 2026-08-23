package com.huk911.apexlegends

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.huk911.apexlegends.models.AmmoType
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Floor
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

const val EXTRA_ITEM = "item"
class ItemDetailActivity : AppCompatActivity() {

    private lateinit var magImage: ImageView
    private lateinit var barrelImage: ImageView
    private lateinit var sightImage: ImageView
    private lateinit var stockImage: ImageView
    private lateinit var slotsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        magImage = findViewById(R.id.img_mag)
        barrelImage = findViewById(R.id.img_barrel)
        sightImage = findViewById(R.id.img_sight)
        stockImage = findViewById(R.id.img_stock)
        slotsTextView = findViewById(R.id.tv_slots)
        val nameCard: TextView = findViewById(R.id.detailNameCard)
        val descriptionCard: TextView = findViewById(R.id.detailDescriptionCard)
        val itemImage: ImageView = findViewById(R.id.detailImage)


        val rawItem = intent.getSerializableExtra(EXTRA_ITEM)
        if (rawItem is Item) {
            nameCard.text = rawItem.name
            val description = buildItemDescription(rawItem)
            descriptionCard.text = description
            val imageResource = pickItemImage(rawItem.name)
            itemImage.setImageResource(imageResource)
            val attachmentResource = pickAttachmentImage(rawItem)
            magImage.setImageResource(attachmentResource)
        } else {
            nameCard.text = "Неизвестный предмет"
        }

        showWeaponSlots()

    }

    fun showWeaponSlots() {
        val rawItem = intent.getSerializableExtra(EXTRA_ITEM)
        if (rawItem is Weapon) {
            magImage.isVisible = true
            slotsTextView.isVisible = true
        } else {
            magImage.isVisible = false
            slotsTextView.isVisible = false
        }
    }


    private fun buildItemDescription(item: Item): String {
        val statsLine = when (item) {
            is Weapon -> "Урон: " + item.damage + "\n" + "Магазин: " + item.magSize + "\n" + "Тип патронов: " + item.ammoType
            is Consumable -> "Лечит: +" + item.healAmount + " HP"
            is Grenade -> "Урон взрыва: " + item.blastDamage
            else -> "Особых свойств нет"
        }
        val valueLine = "Ценность: " + item.calculateValue()
        return "Редкость: " + item.rarity + "\n" + statsLine + "\n" + valueLine
    }

    private fun pickItemImage(itemName: String): Int = when (itemName) {
        "R-301" -> R.drawable.r301
        "Wingman" -> R.drawable.wingman
        "Flatline" -> R.drawable.flatline
        "Syringe" -> R.drawable.apex_syringe
        "Shield Battery" -> R.drawable.shield_battery
        "Arc Star" -> R.drawable.arc_star
        "Thermite" -> R.drawable.thermite
        else -> R.drawable.ic_item_placeholder
    }

    private fun pickAttachmentImage(item: Item): Int = when (item) {
        is Weapon -> when (item.ammoType) {
            AmmoType.LIGHT -> R.drawable.maglight
            AmmoType.HEAVY -> R.drawable.magheavy
            else -> R.drawable.mag_placeholder
        }
        else -> R.drawable.mag_placeholder
    }

//    private fun showHeavyAttachments(weapon: Weapon): Int {
//        return when (weapon.ammoType) {
//            AmmoType.HEAVY ->
//        }
//    }


}
