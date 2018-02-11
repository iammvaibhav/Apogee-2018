package bitspilani.dvm.apogee2016.di.module

import android.content.Context
import android.graphics.Typeface
import bitspilani.dvm.apogee2016.data.AppDataManager
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.di.*
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

    @Provides
    @PerActivity
    fun provideDataManager(dataManager: AppDataManager): DataManager = dataManager

    @Provides
    @PerActivity
    @Light
    fun provideLightFont() = Typeface.createFromAsset(context.assets, "fonts/light.otf")

    @Provides
    @PerActivity
    @Medium
    fun provideMediumFont() = Typeface.createFromAsset(context.assets, "fonts/medium.otf")

    @Provides
    @PerActivity
    @Regular
    fun provideRegularFont() = Typeface.createFromAsset(context.assets, "fonts/regular.otf")

    @Provides
    @PerActivity
    @SemiBold
    fun provideSemiBoldFont(): Typeface = Typeface.createFromAsset(context.assets, "fonts/semiBold.otf")

}

