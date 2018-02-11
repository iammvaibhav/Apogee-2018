package bitspilani.dvm.apogee2016.ui.main

import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.ui.base.MvpPresenter

/**
 * Created by Vaibhav on 29-01-2018.
 */

interface MainMvpPresenter<V : MainMvpView> : MvpPresenter<V> {
    fun getVenueList(exec: (List<String>) -> Unit)
    fun getCategoryList(exec: (List<String>) -> Unit)
    fun fetchQueries(filterEvents: FilterEvents, exec: (List<Pair<String, List<Event>>>) -> Unit)
}