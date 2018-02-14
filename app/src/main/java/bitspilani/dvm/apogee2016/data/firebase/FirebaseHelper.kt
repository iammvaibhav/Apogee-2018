package bitspilani.dvm.apogee2016.data.firebase

import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.firebase.model.Sponsor

/**
 * Created by Vaibhav on 23-01-2018.
 */

interface FirebaseHelper {
    fun getEvents(filterEvents: FilterEvents, exec : (List<Pair<String, List<Event>>>) -> Unit)
    fun getSponsors(exec: (List<Sponsor>) -> Unit)
    fun getVenueList(exec: (List<String>) -> Unit)
    fun getCategoryList(exec: (List<String>) -> Unit)
}