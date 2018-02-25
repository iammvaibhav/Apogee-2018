package org.dvm.bits_apogee

import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.google.firebase.database.FirebaseDatabase
import org.dvm.bits_apogee.data.dataManager

class App : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseDatabase.getInstance().getReference("Events").keepSynced(true)

        AndroidNetworking.initialize(this)

        dataManager.setDataManager(this)
    }
}