package org.dvm.bits_apogee.data

import org.dvm.bits_apogee.data.firebase.FirebaseHelper
import org.dvm.bits_apogee.data.prefs.PreferencesHelper
import org.dvm.bits_apogee.data.prefs.model.CurrentUser

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface DataManager: PreferencesHelper, FirebaseHelper {
    fun setCurrentUser(currentUser: CurrentUser)
    fun getCurrentUser(): CurrentUser
}