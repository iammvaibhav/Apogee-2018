package bitspilani.dvm.apogee2016.ui.login

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.PerActivity
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
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