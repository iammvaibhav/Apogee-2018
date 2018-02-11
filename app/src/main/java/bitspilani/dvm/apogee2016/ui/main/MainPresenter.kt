package bitspilani.dvm.apogee2016.ui.main

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.di.PerActivity
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Vaibhav on 29-01-2018.
 */

@PerActivity
class MainPresenter<V : MainMvpView> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), MainMvpPresenter<V> {

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