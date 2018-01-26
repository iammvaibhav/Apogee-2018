package bitspilani.dvm.apogee2016.ui.splash

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import bitspilani.dvm.apogee2016.utils.AppConstants
import io.realm.*
import javax.inject.Inject

/**
 * Created by Vaibhav on 24-01-2018.
 */

class SplashPresenter<V: SplashMvpView> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), SplashMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        signInUser { user: SyncUser ->
            val config = SyncConfiguration.Builder(user, AppConstants.REALM_DATABASE_ADDRESS)
                    .waitForInitialRemoteData()
                    .build()

            Realm.setDefaultConfiguration(config)
            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    // Realm Instance Ready
                    if (getDataManager().isOnboardingRequired()) {
                        getDataManager().setOnBoardingRequired(false)
                        getMvpView()?.openOnboardingActivity()
                    } else {
                        getMvpView()?.openMainActivity()
                    }

                }
            })
        }
    }

    private fun signInUser(callback: (SyncUser) -> Unit) {
        val currentUser = SyncUser.currentUser()
        if (currentUser == null) {

            val credentials = SyncCredentials.usernamePassword(AppConstants.REALM_SERVER_USERNAME, AppConstants.REALM_SERVER_PASSWORD)

            SyncUser.loginAsync(credentials, AppConstants.REALM_SERVER_ADDRESS, object : SyncUser.Callback<SyncUser> {
                override fun onSuccess(result: SyncUser) {
                    callback(result)
                }

                override fun onError(error: ObjectServerError) {
                    getMvpView()?.onError(error.errorMessage)
                }
            })

        } else {
            // User is already logged in
            callback(currentUser)
        }
    }

}