package org.dvm.bits_apogee.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.dataManager
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.ui.base.BaseActivity
import org.dvm.bits_apogee.ui.main.MainActivity

/**
 * Created by Vaibhav on 24-01-2018.
 */

class SplashActivity : BaseActivity(), SplashMvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.splash_screen)
        FirebaseMessaging.getInstance().subscribeToTopic("all")

        dataManager.getDataManager().getEvents(FilterEvents()) {
            Handler().postDelayed({
                openMainActivity()
            }, 1000)
            /*if (getDataManager().isOnboardingRequired()) {
                getDataManager().setOnBoardingRequired(false)
                getMvpView()?.openOnboardingActivity()
            } else {
                getMvpView()?.openMainActivity()
            }*/
        }
    }


    override fun openOnboardingActivity() {
        Toast.makeText(this, "onboarding", Toast.LENGTH_SHORT).show()
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}