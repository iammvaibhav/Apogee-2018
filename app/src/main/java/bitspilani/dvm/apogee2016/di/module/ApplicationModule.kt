package bitspilani.dvm.apogee2016.di.module

import android.content.Context
import bitspilani.dvm.apogee2016.di.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * Created by Vaibhav on 23-01-2018.
 */

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext() = context

}