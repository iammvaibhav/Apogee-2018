package bitspilani.dvm.apogee2016.data.prefs

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface PreferencesHelper {

    fun addAsFavourite(id: Int)
    fun removeAsFavourite(id: Int)
    fun isFavourite(id: Int): Boolean
    fun getFavouritesArray(): Array<Int>
    fun clearFavourites()
    fun isOnboardingRequired(): Boolean
    fun setOnBoardingRequired(required: Boolean)
}