package org.dvm.bits_apogee

import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.google.firebase.database.FirebaseDatabase
import org.dvm.bits_apogee.di.component.ApplicationComponent
import org.dvm.bits_apogee.di.component.DaggerApplicationComponent
import org.dvm.bits_apogee.di.module.ApplicationModule

class App : MultiDexApplication() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseDatabase.getInstance().getReference("Events").keepSynced(true)

        AndroidNetworking.initialize(this)
    }



    fun getApplicationComponent() = applicationComponent
}