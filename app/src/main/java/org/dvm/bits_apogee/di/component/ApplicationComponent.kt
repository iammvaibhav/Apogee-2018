package org.dvm.bits_apogee.di.component

import android.app.Application
import android.content.Context
import org.dvm.bits_apogee.di.ApplicationContext
import org.dvm.bits_apogee.di.module.ApplicationModule
import dagger.Component

/**
 * Created by Vaibhav on 23-01-2018.
 */

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @ApplicationContext fun getContext(): Context

    fun inject(App: Application)
}