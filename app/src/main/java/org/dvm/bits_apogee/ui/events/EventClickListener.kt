package org.dvm.bits_apogee.ui.events

import org.dvm.bits_apogee.data.firebase.model.Event

/**
 * Created by Vaibhav on 11-02-2018.
 */

interface EventClickListener {
    fun onEventClick(event: Event, isFavourite: Boolean, time: String)
}