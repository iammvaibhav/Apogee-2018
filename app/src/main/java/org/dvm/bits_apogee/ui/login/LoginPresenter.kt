package org.dvm.bits_apogee.ui.login

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.di.PerActivity
import org.dvm.bits_apogee.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Vaibhav on 11-02-2018.
 */

@PerActivity
class LoginPresenter<V: LoginMvpView> @Inject constructor(dataManager: DataManager): BasePresenter<V>(dataManager), LoginMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}