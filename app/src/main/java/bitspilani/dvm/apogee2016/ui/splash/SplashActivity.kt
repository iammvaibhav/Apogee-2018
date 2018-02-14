package bitspilani.dvm.apogee2016.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by Vaibhav on 24-01-2018.
 */

class SplashActivity : BaseActivity(), SplashMvpView {

    @Inject
    lateinit var splashPresenter: SplashPresenter<SplashMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        getActivityComponent().inject(this)
        splashPresenter.onAttach(this)
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