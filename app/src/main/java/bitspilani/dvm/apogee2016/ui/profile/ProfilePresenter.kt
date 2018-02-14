package bitspilani.dvm.apogee2016.ui.profile

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.PerFragment
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import bitspilani.dvm.apogee2016.ui.base.MvpPresenter
import javax.inject.Inject

/**
 * Created by Vaibhav on 12-02-2018.
 */

@PerFragment
class ProfilePresenter<V: ProfileMvpView> @Inject constructor(dataManager: DataManager): BasePresenter<V>(dataManager), MvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
    }

    override fun onDetach() {
        super.onDetach()
    }
}