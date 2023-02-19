package pl.hamsterdev.pott

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import java.util.Date


class CustomAdapter(private val items: List<ItemModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val todayMidnight: Instant
        get() {
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)

            return today.toInstant()
        }

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

        val start = todayMidnight
        val end = Calendar.getInstance()
        end.time = Date(itemModel.expireAt)

        val duration = Duration.between(start, end.toInstant())

        holder.daysLeft.text = duration.toDays().toString()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val quantity: TextView = itemView.findViewById(R.id.item_quantity)
        val daysLeft: TextView = itemView.findViewById(R.id.days_left)

    }
}