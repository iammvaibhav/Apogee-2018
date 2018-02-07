package bitspilani.dvm.apogee2016.di.component

import android.content.Context
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.ActivityContext
import bitspilani.dvm.apogee2016.di.PerActivity
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

    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
}