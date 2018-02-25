package org.dvm.bits_apogee.data.prefs

import android.content.Context
import org.dvm.bits_apogee.ui.informatives.NotificationData


/**
 * Created by Vaibhav on 24-01-2018.
 */

class AppPreferencesHelper constructor(context: Context): PreferencesHelper {

    private val sharedPreferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)

    private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
    private val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"
    private val PREF_KEY_CURRENT_USER_USERNAME = "PREF_KEY_CURRENT_USER_USERNAME"
    private val PREF_KEY_CURRENT_USER_PASSWORD = "PREF_KEY_CURRENT_USER_PASSWORD"
    private val PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME"
    private val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"
    private val PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL"
    private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    private val PREF_KEY_IS_BITSIAN = "PREF_KEY_IS_BITSIAN"
    private val PREF_KEY_SIGNED_EVENTS = "PREF_KEY_SIGNED_EVENTS"
    private val PREF_KEY_QR_CODE = "PREF_KEY_QR_CODE"
    private val PREF_KEY_NOTIFICATION_DATA = "PREF_KEY_NOTIFICATION_DATA"


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

    override fun setIsBitsian(isBitsian: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_IS_BITSIAN, isBitsian).apply()
    }

    override fun getIsBitsian(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_IS_BITSIAN, true)
    }

    override fun setSignedEvents(signedEvents: String) {
        sharedPreferences.edit().putString(PREF_KEY_SIGNED_EVENTS, signedEvents).apply()
    }

    override fun getSignedEvents(): String {
        return sharedPreferences.getString(PREF_KEY_SIGNED_EVENTS, "")
    }

    override fun setQrCode(signedEvents: String) {
        sharedPreferences.edit().putString(PREF_KEY_QR_CODE, signedEvents).apply()
    }

    override fun getQrCode(): String {
        return sharedPreferences.getString(PREF_KEY_QR_CODE, "")
    }

    override fun putNotification(notificationData: NotificationData) {
        val list = getStringSet(PREF_KEY_NOTIFICATION_DATA)
        list.add(notificationData.toString())
        putStringSet(PREF_KEY_NOTIFICATION_DATA, list)
    }

    override fun getNotifications(): List<NotificationData> {
        val notifications = getStringSet(PREF_KEY_NOTIFICATION_DATA).toMutableList().map { NotificationData.fromString(it) }
        notifications.sortedByDescending { it.date }
        return notifications
    }
}
