package bitspilani.dvm.apogee2016.di.component

import android.app.Application
import android.content.Context
import bitspilani.dvm.apogee2016.di.ApplicationContext
import bitspilani.dvm.apogee2016.di.module.ApplicationModule
import dagger.Component

/**
 * Created by Vaibhav on 23-01-2018.
 */

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @ApplicationContext fun getContext(): Context

    fun inject(App: Application)
}