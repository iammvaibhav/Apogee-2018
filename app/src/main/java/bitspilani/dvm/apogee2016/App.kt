package bitspilani.dvm.apogee2016

import android.support.multidex.MultiDexApplication
import bitspilani.dvm.apogee2016.di.component.ApplicationComponent
import bitspilani.dvm.apogee2016.di.component.DaggerApplicationComponent
import bitspilani.dvm.apogee2016.di.module.ApplicationModule
import com.androidnetworking.AndroidNetworking
import com.google.firebase.database.FirebaseDatabase

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