package bitspilani.dvm.apogee2016.data.prefs

import android.content.Context
import android.content.SharedPreferences
import bitspilani.dvm.apogee2016.di.ActivityContext
import bitspilani.dvm.apogee2016.di.PerActivity
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject


/**
 * Created by Vaibhav on 24-01-2018.
 */

@PerActivity
class AppPreferencesHelper : PreferencesHelper {

    private val sharedPreferences: SharedPreferences

    private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
    private val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"
    private val PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME"
    private val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"
    private val PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL"
    private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

    private val PREF_KEY_IS_ONBOARDING_REQUIRED = "PREF_KEY_IS_ONBOARDING_REQUIRED"
    private val PREF_KEY_FAVOURITE_LIST = "PREF_KEY_FAVOURITE_LIST"

    @Inject
    constructor(@ActivityContext context: Context) {
        sharedPreferences = context.defaultSharedPreferences
    }

    override fun addAsFavourite(id: Int) {
        val list = getStringSet(PREF_KEY_FAVOURITE_LIST)
        list.add("$id")
        putStringSet(PREF_KEY_FAVOURITE_LIST, list)
    }

    override fun removeAsFavourite(id: Int) {
        val list = getStringSet(PREF_KEY_FAVOURITE_LIST)
        list.remove("$id")
        putStringSet(PREF_KEY_FAVOURITE_LIST, list)
    }

    override fun getFavouritesArray(): Array<Int> {
        val favouritesSet = getStringSet(PREF_KEY_FAVOURITE_LIST)
        val array = Array(favouritesSet.size) { 0 }
        favouritesSet.forEachIndexed { index, s -> array[index] = s.toInt() }
        return array
    }

    override fun clearFavourites() {
        sharedPreferences.edit().putStringSet(PREF_KEY_FAVOURITE_LIST, HashSet<String>()).apply()
    }

    override fun isOnboardingRequired() = sharedPreferences.getBoolean(PREF_KEY_IS_ONBOARDING_REQUIRED, true)

    override fun setOnBoardingRequired(required: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_IS_ONBOARDING_REQUIRED, required).apply()
    }

    private fun putStringSet(key: String, set: Set<String>) {
        sharedPreferences.edit().putStringSet(key, set).apply()
    }

    private fun getStringSet(key: String) = sharedPreferences.getStringSet(key, HashSet<String>())

    override fun isFavourite(id: Int) = getFavouritesArray().contains(id)
}
