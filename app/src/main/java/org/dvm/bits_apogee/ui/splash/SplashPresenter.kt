package org.dvm.bits_apogee.ui.splash

import android.os.Handler
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.di.PerActivity
import org.dvm.bits_apogee.ui.base.BasePresenter
import javax.inject.Inject


/**
 * Created by Vaibhav on 24-01-2018.
 */

@PerActivity
class SplashPresenter<V : SplashMvpView> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), SplashMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        getDataManager().getEvents(FilterEvents()) {
            Handler().postDelayed({
                getMvpView()?.openMainActivity()
            }, 1000)
            /*if (getDataManager().isOnboardingRequired()) {
                getDataManager().setOnBoardingRequired(false)
                getMvpView()?.openOnboardingActivity()
            } else {
                getMvpView()?.openMainActivity()
            }*/
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}