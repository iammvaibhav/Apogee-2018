package bitspilani.dvm.apogee2016.data

import bitspilani.dvm.apogee2016.data.firebase.AppFirebaseHelper
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.prefs.AppPreferencesHelper
import bitspilani.dvm.apogee2016.di.PerActivity
import javax.inject.Inject

/**
 * Created by Vaibhav on 24-01-2018.
 */

@PerActivity
class AppDataManager @Inject constructor(val firebase: AppFirebaseHelper, val pref: AppPreferencesHelper) : DataManager {

    override fun addAsFavourite(id: Int) { pref.addAsFavourite(id) }

    override fun removeAsFavourite(id: Int) { pref.removeAsFavourite(id) }

    override fun getFavouritesArray() = pref.getFavouritesArray()

    override fun clearFavourites() { pref.clearFavourites() }

    override fun isOnboardingRequired() = pref.isOnboardingRequired()

    override fun setOnBoardingRequired(required: Boolean) { pref.setOnBoardingRequired(required) }

    override fun isFavourite(id: Int) = pref.isFavourite(id)

    override fun getEvents(filterEvents: FilterEvents, exec: (List<Pair<String, List<Event>>>) -> Unit) {
        firebase.getEvents(filterEvents, exec)
    }

    override fun getVenueList(exec: (List<String>) -> Unit) {
        firebase.getVenueList(exec)
    }

    override fun getCategoryList(exec: (List<String>) -> Unit) {
        firebase.getCategoryList(exec)
    }
}