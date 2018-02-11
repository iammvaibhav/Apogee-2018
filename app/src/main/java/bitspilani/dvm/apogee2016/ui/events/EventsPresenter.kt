package bitspilani.dvm.apogee2016.ui.events

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.PerFragment
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Vaibhav on 04-02-2018.
 */

@PerFragment
class EventsPresenter<V: EventsMvpView> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), EventsMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}