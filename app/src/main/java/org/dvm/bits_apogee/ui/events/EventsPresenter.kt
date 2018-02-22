package org.dvm.bits_apogee.ui.events

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.di.PerFragment
import org.dvm.bits_apogee.ui.base.BasePresenter
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