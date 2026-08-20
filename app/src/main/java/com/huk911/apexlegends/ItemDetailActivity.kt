package com.huk911.apexlegends

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.Weapon

const val EXTRA_ITEM = "item"
class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val nameCard: TextView = findViewById(R.id.detailNameCard)
        val descriptionCard: TextView = findViewById(R.id.detailDescriptionCard)
        val itemImage: ImageView = findViewById(R.id.detailImage)
        val magImage: ImageView = findViewById(R.id.img_mag)

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
            "Light" -> R.drawable.maglight
            "Heavy" -> R.drawable.magheavy
            else -> R.drawable.mag_placeholder
        }
        else -> R.drawable.mag_placeholder
    }


}
