package bitspilani.dvm.apogee2016.di.component

import android.app.Application
import bitspilani.dvm.apogee2016.di.module.ApplicationModule
import dagger.Component

/**
 * Created by Vaibhav on 23-01-2018.
 */

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(App: Application)
}