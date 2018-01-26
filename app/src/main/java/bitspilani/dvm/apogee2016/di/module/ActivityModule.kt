package bitspilani.dvm.apogee2016.di.module

import android.content.Context
import bitspilani.dvm.apogee2016.data.AppDataManager
import bitspilani.dvm.apogee2016.di.ActivityContext
import bitspilani.dvm.apogee2016.di.PerActivity
import bitspilani.dvm.apogee2016.ui.splash.SplashMvpView
import bitspilani.dvm.apogee2016.ui.splash.SplashPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Vaibhav on 23-01-2018.
 */

@Module
class ActivityModule(val context: Context) {

    @Provides
    @ActivityContext
    @PerActivity
    fun provideActivityContext() = context

    /*@Provides
    @PerActivity
    fun provideDataManager(dataManager: AppDataManager): DataManager =  dataManager*/

    @Provides
    @PerActivity
    fun provideSplashPresenter(dataManager: AppDataManager) = SplashPresenter<SplashMvpView>(dataManager)

}