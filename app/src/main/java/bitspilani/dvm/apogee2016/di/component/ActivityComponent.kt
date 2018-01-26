package bitspilani.dvm.apogee2016.di.component

import bitspilani.dvm.apogee2016.di.PerActivity
import bitspilani.dvm.apogee2016.di.module.ActivityModule
import bitspilani.dvm.apogee2016.ui.splash.SplashActivity
import dagger.Component

/**
 * Created by Vaibhav on 23-01-2018.
 */

@PerActivity
@Component(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(splashActivity: SplashActivity)
}