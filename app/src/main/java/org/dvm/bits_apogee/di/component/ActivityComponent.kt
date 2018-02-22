package org.dvm.bits_apogee.di.component

import android.content.Context
import android.graphics.Typeface
import dagger.Component
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.di.*
import org.dvm.bits_apogee.di.module.ActivityModule
import org.dvm.bits_apogee.ui.login.LoginActivity
import org.dvm.bits_apogee.ui.main.MainActivity
import org.dvm.bits_apogee.ui.splash.SplashActivity

/**
 * Created by Vaibhav on 23-01-2018.
 */

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    @ActivityContext fun getContext(): Context
    fun getDataManager(): DataManager
    @Light fun getLightFont(): Typeface
    @Medium fun getMediumFont(): Typeface
    @Regular fun getRegularFont(): Typeface
    @SemiBold fun getSemiBoldFont(): Typeface


    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
}