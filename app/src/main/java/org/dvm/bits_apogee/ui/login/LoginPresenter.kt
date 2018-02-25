package org.dvm.bits_apogee.ui.login

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.ui.base.BasePresenter

/**
 * Created by Vaibhav on 11-02-2018.
 */

class LoginPresenter<V: LoginMvpView> constructor(dataManager: DataManager): BasePresenter<V>(dataManager), LoginMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}