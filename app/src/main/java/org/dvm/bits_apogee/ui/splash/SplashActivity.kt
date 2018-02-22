package org.dvm.bits_apogee.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.ui.base.BaseActivity
import org.dvm.bits_apogee.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by Vaibhav on 24-01-2018.
 */

class SplashActivity : BaseActivity(), SplashMvpView {

    @Inject
    lateinit var splashPresenter: SplashPresenter<SplashMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.splash_screen)

        getActivityComponent().inject(this)
        splashPresenter.onAttach(this)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    override fun onDestroy() {
        super.onDestroy()
        splashPresenter.onDetach()
    }

    override fun openOnboardingActivity() {
        Toast.makeText(this, "onboarding", Toast.LENGTH_SHORT).show()
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}