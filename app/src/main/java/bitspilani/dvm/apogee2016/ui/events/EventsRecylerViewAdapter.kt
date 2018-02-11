package bitspilani.dvm.apogee2016.ui.events

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import com.sackcentury.shinebuttonlib.ShineButton
import java.text.SimpleDateFormat

/**
 * Created by Vaibhav on 06-02-2018.
 */

class EventsRecylerViewAdapter(val eventsData: List<Event>, val showBy: Int, val dataManager: DataManager,
                               val lightFont: Typeface, val regularFont: Typeface) : RecyclerView.Adapter<EventsRecylerViewAdapter.Companion.EventsRecyclerViewViewHolder>(){

    companion object {
        class EventsRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val eventName = itemView.findViewById<TextView>(R.id.eventName)
            val eventVenue = itemView.findViewById<TextView>(R.id.venue)
            val eventTime = itemView.findViewById<TextView>(R.id.time)
            val markFavourite = itemView.findViewById<ShineButton>(R.id.mark_favourite)

        }
    }

    val parse = SimpleDateFormat("HH:mm")
    val shows0 = SimpleDateFormat("hh:mm a")
    val shows1 = SimpleDateFormat("MMM d, hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventsRecyclerViewViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false)
        val holder = EventsRecyclerViewViewHolder(itemView)
        holder.eventName.typeface = regularFont
        holder.eventTime.typeface = lightFont
        holder.eventVenue.typeface = lightFont
        return holder
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewViewHolder, position: Int) {
        // showBy == 0 -> by date & 1 -> by category

        val event = eventsData[position]

        //setting basic data
        holder.eventName.text = event.name
        holder.eventVenue.text = event.venue

        try {
            if (showBy == 0) {
                holder.eventTime.text = shows0.format(parse.parse(event.startTime))
            } else {
                holder.eventTime.text = shows1.format(parse.parse(event.startTime))
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

        //decide if to show as favourite or not
        holder.markFavourite.isChecked = dataManager.isFavourite(event.id)

        holder.markFavourite.setOnCheckStateChangeListener { view, checked ->
            if (checked)
                dataManager.addAsFavourite(event.id)
            else
                dataManager.removeAsFavourite(event.id)
        }
    }

    override fun getItemCount() = eventsData.size
}