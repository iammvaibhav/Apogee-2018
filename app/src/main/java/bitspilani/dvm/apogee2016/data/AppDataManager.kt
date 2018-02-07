package bitspilani.dvm.apogee2016.data

import bitspilani.dvm.apogee2016.data.prefs.AppPreferencesHelper
import bitspilani.dvm.apogee2016.data.realmdb.AppRealmDbHelper
import bitspilani.dvm.apogee2016.di.PerActivity
import javax.inject.Inject

/**
 * Created by Vaibhav on 24-01-2018.
 */

@PerActivity
class AppDataManager @Inject constructor(val realmDb: AppRealmDbHelper, val pref: AppPreferencesHelper) : DataManager {

    override fun addAsFavourite(id: Int) { pref.addAsFavourite(id) }

    override fun removeAsFavourite(id: Int) { pref.removeAsFavourite(id) }

    override fun getFavouritesArray() = pref.getFavouritesArray()

    override fun clearFavourites() { pref.clearFavourites() }

    override fun isOnboardingRequired() = pref.isOnboardingRequired()

    override fun setOnBoardingRequired(required: Boolean) { pref.setOnBoardingRequired(required) }

    override fun isFavourite(id: Int) = pref.isFavourite(id)
}