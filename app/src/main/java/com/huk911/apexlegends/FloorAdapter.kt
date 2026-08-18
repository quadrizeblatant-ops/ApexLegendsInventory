package com.huk911.apexlegends

import android.view.View
import android.widget.TextView
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huk911.apexlegends.models.Floor
import com.huk911.apexlegends.models.Rarity
import com.huk911.apexlegends.models.SelectionWatcher

class FloorAdapter (private val floor: Floor) :
    RecyclerView.Adapter<FloorAdapter.FloorViewHolder>() {
    var selectionWatcher: SelectionWatcher? = null
    class FloorViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView) {
        val rowCardFloor: TextView = rowView.findViewById(R.id.tv_row_item_floor)
        val valueCard: TextView = rowView.findViewById(R.id.tv_value)
    }

    override fun getItemCount(): Int = floor.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowView = inflater.inflate(R.layout.floor_row, parent, false)
        return FloorViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: FloorViewHolder, position: Int) {
        val item = floor.items[position]
        holder.rowCardFloor.text = item.name
        holder.valueCard.text = "Ценность: " + item.calculateValue()

        if (item.rarity == Rarity.LEGENDARY) {
            holder.rowCardFloor.setTextColor(pickRarityColor(item.rarity))
        } else holder.rowCardFloor.setTextColor(Color.BLACK)

        val isSelected = (item === floor.currentSelectedItem)
        if (isSelected) {
            holder.rowView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.rowView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.rowView.setOnClickListener {
            floor.selectSlot(position)
            selectionWatcher?.onItemSelected(item)
//            notifyDataSetChanged()
        }
    }

}

    private fun pickRarityColor(rarity: Rarity): Int = when (rarity) {
        Rarity.COMMON -> Color.GRAY
        Rarity.RARE -> Color.BLUE
        Rarity.EPIC -> Color.MAGENTA
        Rarity.LEGENDARY -> Color.RED
    }
