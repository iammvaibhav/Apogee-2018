package org.dvm.bits_apogee.ui.profile

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.ui.base.BasePresenter
import org.dvm.bits_apogee.ui.base.MvpPresenter

/**
 * Created by Vaibhav on 12-02-2018.
 */

class ProfilePresenter<V: ProfileMvpView> constructor(dataManager: DataManager): BasePresenter<V>(dataManager), MvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}