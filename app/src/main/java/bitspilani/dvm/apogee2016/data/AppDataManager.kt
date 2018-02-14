package bitspilani.dvm.apogee2016.data

import bitspilani.dvm.apogee2016.data.firebase.AppFirebaseHelper
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.firebase.model.Sponsor
import bitspilani.dvm.apogee2016.data.prefs.AppPreferencesHelper
import bitspilani.dvm.apogee2016.data.prefs.model.CurrentUser
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

    override fun getSponsors(exec: (List<Sponsor>) -> Unit) {
        firebase.getSponsors(exec)
    }

    override fun setUserLoggedIn(loggedIn: Boolean) {
        pref.setUserLoggedIn(loggedIn)
    }

    override fun getUserLoggedIn(): Boolean {
        return pref.getUserLoggedIn()
    }

    override fun setCurrentUserId(id: String) {
        pref.setCurrentUserId(id)
    }

    override fun getCurrentUserId(): String {
        return pref.getCurrentUserId()
    }

    override fun setCurrentUserUsername(username: String) {
        pref.setCurrentUserUsername(username)
    }

    override fun getCurrentUserUsername(): String {
        return pref.getCurrentUserUsername()
    }

    override fun setCurrentUserPassword(password: String) {
        pref.setCurrentUserPassword(password)
    }

    override fun getCurrentUserPassword(): String {
        return pref.getCurrentUserPassword()
    }

    override fun setCurrentUserName(name: String) {
        pref.setCurrentUserName(name)
    }

    override fun getCurrentUserName(): String {
        return pref.getCurrentUserName()
    }

    override fun setCurrentUserEmail(email: String) {
        pref.setCurrentUserEmail(email)
    }

    override fun getCurrentUserEmail(): String {
        return pref.getCurrentUserEmail()
    }

    override fun setCurrentUserProfileURL(url: String) {
        pref.setCurrentUserProfileURL(url)
    }

    override fun getCurrentUserProfileURL(): String {
        return pref.getCurrentUserProfileURL()
    }

    override fun setCurrentUserAccessToken(token: String) {
        pref.setCurrentUserAccessToken(token)
    }

    override fun getCurrentUserAccessToken(): String {
        return pref.getCurrentUserAccessToken()
    }

    override fun setCurrentUser(currentUser: CurrentUser) {
        setUserLoggedIn(currentUser.loggedIn)
        setCurrentUserId(currentUser.userId)
        setCurrentUserUsername(currentUser.username)
        setCurrentUserPassword(currentUser.password)
        setCurrentUserName(currentUser.name)
        setCurrentUserEmail(currentUser.email)
        setCurrentUserProfileURL(currentUser.profileURL)
        setCurrentUserAccessToken(currentUser.accessToken)
    }

    override fun getCurrentUser(): CurrentUser {
        return CurrentUser(getUserLoggedIn(), getCurrentUserId(), getCurrentUserUsername(), getCurrentUserPassword(),
                getCurrentUserName(), getCurrentUserEmail(), getCurrentUserProfileURL(), getCurrentUserAccessToken())
    }

    override fun setIsBitsian(isBitsian: Boolean) {
        pref.setIsBitsian(isBitsian)
    }

    override fun getIsBitsian(): Boolean {
        return pref.getIsBitsian()
    }

    override fun setSignedEvents(signedEvents: String) {
        pref.setSignedEvents(signedEvents)
    }

    override fun getSignedEvents(): String {
        return pref.getSignedEvents()
    }

    override fun setQrCode(qr: String) {
        pref.setQrCode(qr)
    }

    override fun getQrCode(): String {
        return pref.getQrCode()
    }
}