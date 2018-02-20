package bitspilani.dvm.apogee2016.ui.splash

import android.os.Handler
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.di.PerActivity
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
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