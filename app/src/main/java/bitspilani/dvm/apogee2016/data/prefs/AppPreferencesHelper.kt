package bitspilani.dvm.apogee2016.data.prefs

import android.content.Context
import bitspilani.dvm.apogee2016.di.ApplicationContext
import bitspilani.dvm.apogee2016.di.PerActivity
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject


/**
 * Created by Vaibhav on 24-01-2018.
 */

@PerActivity
class AppPreferencesHelper @Inject constructor(@ApplicationContext context: Context): PreferencesHelper {

    private val sharedPreferences = context.defaultSharedPreferences

    private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
    private val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"
    private val PREF_KEY_CURRENT_USER_USERNAME = "PREF_KEY_CURRENT_USER_USERNAME"
    private val PREF_KEY_CURRENT_USER_PASSWORD = "PREF_KEY_CURRENT_USER_PASSWORD"
    private val PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME"
    private val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"
    private val PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL"
    private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

    private val PREF_KEY_IS_ONBOARDING_REQUIRED = "PREF_KEY_IS_ONBOARDING_REQUIRED"
    private val PREF_KEY_FAVOURITE_LIST = "PREF_KEY_FAVOURITE_LIST"

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

    override fun setUserLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, loggedIn).apply()
    }

    override fun getUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_USER_LOGGED_IN_MODE, false)
    }

    override fun setCurrentUserId(id: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_ID, id).apply()
    }

    override fun getCurrentUserId(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_ID, "")
    }

    override fun setCurrentUserUsername(username: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_USERNAME, username).apply()
    }

    override fun getCurrentUserUsername(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_USERNAME, "")
    }

    override fun setCurrentUserPassword(password: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_PASSWORD, password).apply()
    }

    override fun getCurrentUserPassword(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_PASSWORD, "")
    }

    override fun setCurrentUserName(name: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_NAME, name).apply()
    }

    override fun getCurrentUserName(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_NAME, "")
    }

    override fun setCurrentUserEmail(email: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply()
    }

    override fun getCurrentUserEmail(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_EMAIL, "")
    }

    override fun setCurrentUserProfileURL(url: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, url).apply()
    }

    override fun getCurrentUserProfileURL(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, "")
    }

    override fun setCurrentUserAccessToken(token: String) {
        sharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
    }

    override fun getCurrentUserAccessToken(): String {
        return sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, "")
    }
}
