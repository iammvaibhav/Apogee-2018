package org.dvm.bits_apogee.ui.main

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.data.firebase.model.Event
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.ui.base.BasePresenter

/**
 * Created by Vaibhav on 29-01-2018.
 */


class MainPresenter<V : MainMvpView> constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), MainMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun getVenueList(exec: (List<String>) -> Unit) {
        getDataManager().getVenueList(exec)
    }

    override fun getCategoryList(exec: (List<String>) -> Unit) {
        getDataManager().getCategoryList(exec)
    }

    override fun fetchQueries(filterEvents: FilterEvents, exec: (List<Pair<String, List<Event>>>) -> Unit) {
        getDataManager().getEvents(filterEvents, exec)
    }
}