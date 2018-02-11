package bitspilani.dvm.apogee2016.data.firebase

import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.firebase.model.ShowBy
import bitspilani.dvm.apogee2016.data.prefs.AppPreferencesHelper
import bitspilani.dvm.apogee2016.di.PerActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Vaibhav on 23-01-2018.
 */

@PerActivity
class AppFirebaseHelper @Inject constructor(val pref: AppPreferencesHelper) : FirebaseHelper {

    private val dateFormat = SimpleDateFormat("dd M yyyy HH:mm")
    private val data = mutableListOf<Pair<String, List<Event>>>()
    private val found = mutableListOf<String>()
    private var favourites = pref.getFavouritesArray()

    override fun getEvents(filterEvents: FilterEvents, exec: (List<Pair<String, List<Event>>>) -> Unit) {
        val reference = FirebaseDatabase.getInstance().getReference("Events")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(snapshot : DataSnapshot) {
                data.clear()
                found.clear()
                favourites = pref.getFavouritesArray()

                for (child in snapshot.children) {
                    val event = child.getValue(Event::class.java)
                    if ((event != null) && checkIfNotExcluded(event, filterEvents)) {
                        if ((filterEvents.filterByOngoing && isOngoing(event)) ||
                                (filterEvents.filterByFavourites && isFavourite(event)) ||
                                (!(filterEvents.filterByOngoing || filterEvents.filterByFavourites))) {
                            when(filterEvents.showBy) {
                                ShowBy.DATE -> { insertIfShowByDate(event) }
                                ShowBy.CATEGORY -> { insertIfShowByCategory(event)}
                            }
                        }
                    }
                }
                exec(data)
            }
        })
    }

    override fun getVenueList(exec: (List<String>) -> Unit) {
        val set = mutableSetOf<String>()
        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val event = it.getValue(Event::class.java)
                    if (event != null)
                        set.add(event.venue)
                }

                val list = set.toMutableList()
                list.sort()
                exec(list)
            }
        })
    }

    override fun getCategoryList(exec: (List<String>) -> Unit) {
        val set = mutableSetOf<String>()
        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val event = it.getValue(Event::class.java)
                    if (event != null)
                        set.add(event.category)
                }

                val list = set.toMutableList()
                list.sort()
                exec(list)
            }
        })
    }

    private fun isFavourite(event: Event) = favourites.contains(event.id)

    private fun isOngoing(event: Event): Boolean {
        try {
            val start = dateFormat.parse("${event.day} 2 2017 ${event.startTime}")
            val end = dateFormat.parse("${event.day} 2 2017 ${event.startTime}")
            val curr = Date()

            return curr.after(start) && curr.before(end)
        }catch (e: Exception) {
           e.printStackTrace()
        }
        return false
    }

    private fun insertIfShowByDate(event: Event) {
        if (!found.contains("FEBRUARY ${event.day}")) {
            found.add("FEBRUARY ${event.day}")
            data.add(Pair("FEBRUARY ${event.day}", mutableListOf()))
        }

        for (pair in data) {
            if (pair.first == "FEBRUARY ${event.day}") {
                (pair.second as MutableList).add(event)
                break
            }
        }
    }

    private fun insertIfShowByCategory(event: Event) {
        if (!found.contains(event.category)) {
            found.add(event.category)
            data.add(Pair(event.category, mutableListOf()))
        }

        for (pair in data) {
            if (pair.first == event.category) {
                (pair.second as MutableList).add(event)
                break
            }
        }
    }

    private fun checkIfNotExcluded(event: Event, filterEvents: FilterEvents): Boolean {
        return !(filterEvents.excludeCategory.contains(event.category) || filterEvents.excludeVenue.contains(event.venue))
    }

}