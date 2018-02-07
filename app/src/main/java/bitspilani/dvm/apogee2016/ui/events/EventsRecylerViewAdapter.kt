package bitspilani.dvm.apogee2016.ui.events

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.realmdb.model.Event
import com.chauthai.swipereveallayout.SwipeRevealLayout
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vaibhav on 06-02-2018.
 */

class EventsRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val eventName = itemView.findViewById<TextView>(R.id.eventName)
    val eventVenue = itemView.findViewById<TextView>(R.id.venue)
    val eventTime = itemView.findViewById<TextView>(R.id.time)
    val swipeRevealLayout = itemView.findViewById<SwipeRevealLayout>(R.id.swipeRevealLayout)
    val markFavourite = itemView.findViewById<ImageView>(R.id.mark_fav)
    val showFavourite = itemView.findViewById<ImageView>(R.id.show_favourite)
}

class EventsRecylerViewAdapter(eventsData: OrderedRealmCollection<Event>, val showBy: Int, val dataManager: DataManager) :
        RealmRecyclerViewAdapter<Event, EventsRecyclerViewViewHolder>(eventsData, true, true) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventsRecyclerViewViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false)
        return EventsRecyclerViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewViewHolder, position: Int) {
        // showBy == 0 -> by date & 1 -> by category

        val event = getItem(position)!!

        //setting basic data
        holder.eventName.text = event.name
        holder.eventVenue.text = event.venue
        if (showBy == 0) {
            holder.eventTime.text = SimpleDateFormat("hh:mm a").format(event.startTime)
        }else {
            holder.eventTime.text = SimpleDateFormat("MMM d, hh:mm a").format(event.startTime)
        }

        Log.e("fav", Arrays.toString(dataManager.getFavouritesArray()))

        //decide if to show as favourite or not
        if (dataManager.isFavourite(event.id)) {
            holder.showFavourite.visibility = View.VISIBLE
        }else {
            holder.showFavourite.visibility = View.INVISIBLE
        }

        // on swipeRevealLayout close action, check if user has added it as favourite. If added, set favourite icon to visible
        /*holder.swipeRevealLayout.setSwipeListener(object : SwipeRevealLayout.SimpleSwipeListener() {
            override fun onClosed(view: SwipeRevealLayout?) {
                if (dataManager.isFavourite(event.id)) {
                    holder.showFavourite.visibility = View.VISIBLE
                }else {
                    holder.showFavourite.visibility = View.INVISIBLE
                }
            }
        })*/

        holder.markFavourite.setOnClickListener {
            if (dataManager.isFavourite(event.id)) {
                dataManager.removeAsFavourite(event.id)
                holder.showFavourite.visibility = View.INVISIBLE
            }else {
                dataManager.addAsFavourite(event.id)
                holder.showFavourite.visibility = View.VISIBLE
            }
            Log.e("dfav", Arrays.toString(dataManager.getFavouritesArray()))
            holder.swipeRevealLayout.close(true)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.toLong() ?: 0L
    }
}