package org.dvm.bits_apogee.di.module

import android.content.Context
import android.graphics.Typeface
import dagger.Module
import dagger.Provides
import org.dvm.bits_apogee.data.AppDataManager
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.di.*

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

