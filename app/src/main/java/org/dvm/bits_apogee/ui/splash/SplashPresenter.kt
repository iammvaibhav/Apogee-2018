package org.dvm.bits_apogee.ui.splash

import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.ui.base.BasePresenter


/**
 * Created by Vaibhav on 24-01-2018.
 */

class SplashPresenter<V : SplashMvpView> constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), SplashMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)


    }

    override fun onDetach() {
        super.onDetach()
    }

}