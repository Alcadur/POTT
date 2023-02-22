package pl.hamsterdev.pott

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(private val items: List<ItemModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemModel = items[position]

        holder.itemName.text = itemModel.name
        holder.quantity.text = itemModel.quantity.toString()

        val durationInDays = itemModel.duration.toDays()
        if (durationInDays < 0) {
            holder.row.setBackgroundColor(0x73FF0000)
        }
        if (durationInDays == 0L) {
            holder.row.setBackgroundColor(0x66FFC100)
        }
        holder.daysLeft.text = durationInDays.toString()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val row: LinearLayout = itemView.findViewById(R.id.row)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val quantity: TextView = itemView.findViewById(R.id.item_quantity)
        val daysLeft: TextView = itemView.findViewById(R.id.days_left)
    }
}