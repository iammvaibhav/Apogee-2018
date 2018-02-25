package org.dvm.bits_apogee.ui.events

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.ui.base.BasePresenter

/**
 * Created by Vaibhav on 04-02-2018.
 */


class EventsPresenter<V: EventsMvpView> constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), EventsMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}