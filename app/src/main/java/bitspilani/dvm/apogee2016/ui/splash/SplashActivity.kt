package bitspilani.dvm.apogee2016.ui.splash

import android.os.Bundle
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
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
        toast("Open onboarding activity")
    }

    override fun openMainActivity() {
        startActivity<MainActivity>()
    }
}