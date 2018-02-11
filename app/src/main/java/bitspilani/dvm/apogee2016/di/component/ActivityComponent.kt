package bitspilani.dvm.apogee2016.di.component

import android.content.Context
import android.graphics.Typeface
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.*
import bitspilani.dvm.apogee2016.di.module.ActivityModule
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import bitspilani.dvm.apogee2016.ui.splash.SplashActivity
import dagger.Component

/**
 * Created by Vaibhav on 23-01-2018.
 */

@PerActivity
@Component(modules = [ActivityModule::class])
interface ActivityComponent {

    @ActivityContext fun getContext(): Context
    fun getDataManager(): DataManager
    @Light fun getLightFont(): Typeface
    @Medium fun getMediumFont(): Typeface
    @Regular fun getRegularFont(): Typeface
    @SemiBold fun getSemiBoldFont(): Typeface


    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
}