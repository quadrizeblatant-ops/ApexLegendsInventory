package com.huk911.apexlegends


import android.view.View
import android.widget.TextView
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.models.Rarity
class BackpackAdapter(private val inventory: Inventory) :
    RecyclerView.Adapter<BackpackAdapter.BackpackViewHolder>() {

    class BackpackViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView) {
        val rowCard: TextView = rowView.findViewById(R.id.tv_row_item)
    }

    override fun getItemCount(): Int = inventory.backpack.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackpackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowView = inflater.inflate(R.layout.item_row, parent, false)
        return BackpackViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: BackpackViewHolder, position: Int) {
        val item = inventory.backpack[position]
        holder.rowCard.text = item.toString()
        holder.rowCard.setTextColor(pickRarityColor(item.rarity))
        val isSelected = (item === inventory.shownItem)
        if (isSelected) {
            holder.rowView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.rowView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.rowView.setOnClickListener {
            inventory.selectSlot(position)
            notifyDataSetChanged()
        }
    }

}

    private fun pickRarityColor(rarity: Rarity): Int = when (rarity) {
        Rarity.COMMON -> Color.GRAY
        Rarity.RARE -> Color.BLUE
        Rarity.EPIC -> Color.MAGENTA
        Rarity.LEGENDARY -> Color.RED
    }

