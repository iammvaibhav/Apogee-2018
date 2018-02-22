package org.dvm.bits_apogee.utils

import com.awesomecorp.sammy.apogeewallet.utils.URLS

/**
 * Created by Vaibhav on 23-01-2018.
 */

object URL {

    private const val server = URLS.baseIP
    private const val apogee = "https://bits-apogee.org/2018"
    const val GET_USERNAME = "${server}bt"
    const val API_TOKEN = "${server}api_token"
    const val GET_PROFILE = "${server}get_profile"

}