package bitspilani.dvm.apogee2016.data

import bitspilani.dvm.apogee2016.data.firebase.FirebaseHelper
import bitspilani.dvm.apogee2016.data.prefs.PreferencesHelper
import bitspilani.dvm.apogee2016.data.prefs.model.CurrentUser

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface DataManager: PreferencesHelper, FirebaseHelper {
    fun setCurrentUser(currentUser: CurrentUser)
    fun getCurrentUser(): CurrentUser
}