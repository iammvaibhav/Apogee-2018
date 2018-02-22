package org.dvm.bits_apogee.ui.main

import org.dvm.bits_apogee.data.firebase.model.Event
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.ui.base.MvpPresenter

/**
 * Created by Vaibhav on 29-01-2018.
 */

interface MainMvpPresenter<V : MainMvpView> : MvpPresenter<V> {
    fun getVenueList(exec: (List<String>) -> Unit)
    fun getCategoryList(exec: (List<String>) -> Unit)
    fun fetchQueries(filterEvents: FilterEvents, exec: (List<Pair<String, List<Event>>>) -> Unit)
}