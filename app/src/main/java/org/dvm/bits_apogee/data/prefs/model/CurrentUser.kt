package org.dvm.bits_apogee.data.prefs.model

/**
 * Created by Vaibhav on 12-02-2018.
 */

data class CurrentUser(val loggedIn: Boolean, val userId: String, val username: String, val password: String,
                       val name: String, val email: String, val profileURL: String, val accessToken: String)