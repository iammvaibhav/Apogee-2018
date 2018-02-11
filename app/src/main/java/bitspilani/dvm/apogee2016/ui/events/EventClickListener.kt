package bitspilani.dvm.apogee2016.ui.events

import bitspilani.dvm.apogee2016.data.firebase.model.Event

/**
 * Created by Vaibhav on 11-02-2018.
 */

interface EventClickListener {
    fun onEventClick(event: Event, isFavourite: Boolean, time: String)
}