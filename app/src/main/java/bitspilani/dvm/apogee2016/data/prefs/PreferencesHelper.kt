package bitspilani.dvm.apogee2016.data.prefs

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface PreferencesHelper {

    fun addAsFavourite(id: Int)
    fun removeAsFavourite(id: Int)
    fun isFavourite(id: Int): Boolean
    fun getFavouritesArray(): Array<Int>
    fun clearFavourites()
    fun isOnboardingRequired(): Boolean
    fun setOnBoardingRequired(required: Boolean)

    fun setUserLoggedIn(loggedIn: Boolean)
    fun getUserLoggedIn(): Boolean
    fun setCurrentUserId(id: String)
    fun getCurrentUserId(): String
    fun setCurrentUserUsername(username: String)
    fun getCurrentUserUsername(): String
    fun setCurrentUserPassword(password: String)
    fun getCurrentUserPassword(): String
    fun setCurrentUserName(name: String)
    fun getCurrentUserName(): String
    fun setCurrentUserEmail(email: String)
    fun getCurrentUserEmail(): String
    fun setCurrentUserProfileURL(url: String)
    fun getCurrentUserProfileURL(): String
    fun setCurrentUserAccessToken(token: String)
    fun getCurrentUserAccessToken(): String
    fun setIsBitsian(isBitsian: Boolean)
    fun getIsBitsian(): Boolean
    fun setSignedEvents(signedEvents: String)
    fun getSignedEvents(): String
    fun setQrCode(qr: String)
    fun getQrCode(): String

}